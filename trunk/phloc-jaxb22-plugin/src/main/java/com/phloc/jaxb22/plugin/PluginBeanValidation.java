package com.phloc.jaxb22.plugin;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.xml.sax.ErrorHandler;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.math.MathHelper;
import com.phloc.commons.string.StringParser;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.impl.AttributeUseImpl;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.impl.ParticleImpl;
import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;
import com.sun.xml.xsom.impl.parser.DelayedRef;

/**
 * big thanks to original author: cocorossello
 * 
 * @author Philip Helger
 */
public class PluginBeanValidation extends Plugin
{
  private static final String PLUGIN_OPTION_NAME = "Xphloc-bean-validation";
  private static final String JSR_349 = PLUGIN_OPTION_NAME + ":JSR_349";
  private static final String GENERATE_NOT_NULL_ANNOTATIONS = PLUGIN_OPTION_NAME + ":generateNotNullAnnotations";
  private static final BigInteger UNBOUNDED = BigInteger.valueOf (XSParticle.UNBOUNDED);
  private static final String [] NUMBER_TYPES = new String [] { "BigDecimal",
                                                               "BigInteger",
                                                               "String",
                                                               "byte",
                                                               "short",
                                                               "int",
                                                               "long" };

  private boolean bJSR349 = false;
  private boolean bNotNullAnnotations = true;

  @Override
  public String getOptionName ()
  {
    return PLUGIN_OPTION_NAME;
  }

  @Override
  public int parseArgument (final Options opt, final String [] args, final int i) throws BadCommandLineException,
                                                                                 IOException
  {
    final String sArg = args[i];
    int nConsumed = 0;
    int nIndex;

    nIndex = sArg.indexOf (JSR_349);
    if (nIndex > 0)
    {
      bJSR349 = Boolean.parseBoolean (sArg.substring (nIndex + JSR_349.length () + 1));
      nConsumed++;
    }

    nIndex = sArg.indexOf (GENERATE_NOT_NULL_ANNOTATIONS);
    if (nIndex > 0)
    {
      bNotNullAnnotations = Boolean.parseBoolean (sArg.substring (nIndex + GENERATE_NOT_NULL_ANNOTATIONS.length () + 1));
      nConsumed++;
    }

    return nConsumed;
  }

  @Override
  public List <String> getCustomizationURIs ()
  {
    return ContainerHelper.newUnmodifiableList (CJAXB22.NSURI_PHLOC);
  }

  @Override
  public String getUsage ()
  {
    return "  -" +
           PLUGIN_OPTION_NAME +
           "      :  inject Bean validation annotations (JSR 303)\n" +
           "  -" +
           JSR_349 +
           "      :  inject Bean validation annotations (JSR 349)\n" +
           "  -" +
           GENERATE_NOT_NULL_ANNOTATIONS +
           "      :  inject Bean validation annotations (JSR 303) and add @NotNull annotations\n";
  }

  @Override
  public boolean run (final Outline aModel, final Options aOpt, final ErrorHandler errorHandler)
  {
    try
    {
      for (final ClassOutline co : aModel.getClasses ())
      {
        final List <CPropertyInfo> properties = co.target.getProperties ();
        for (final CPropertyInfo property : properties)
        {
          if (property instanceof CElementPropertyInfo)
            _processElement ((CElementPropertyInfo) property, co);
          else
            if (property instanceof CAttributePropertyInfo)
              _processAttribute ((CAttributePropertyInfo) property, co);
            else
              if (property instanceof CValuePropertyInfo)
                _processAttribute ((CValuePropertyInfo) property, co);
              else
                System.err.println ("Unsupported property: " + property);
        }
      }

      return true;
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
      return false;
    }
  }

  /*
   * XS:Element
   */
  private void _processElement (final CElementPropertyInfo aElement, final ClassOutline aClassOutline)
  {
    final XSComponent schemaComponent = aElement.getSchemaComponent ();
    final ParticleImpl aParticle = (ParticleImpl) schemaComponent;
    final BigInteger aMinOccurs = aParticle.getMinOccurs ();
    final BigInteger aMaxOccurs = aParticle.getMaxOccurs ();
    final JFieldVar aField = aClassOutline.implClass.fields ().get (aElement.getName (false));

    // workaround for choices
    final boolean bRequired = aElement.isRequired ();
    if (MathHelper.isLowerThanZero (aMinOccurs) || aMinOccurs.compareTo (BigInteger.ONE) >= 0 && bRequired)
    {
      if (bNotNullAnnotations && !_hasAnnotation (aField, NotNull.class))
      {
        aField.annotate (NotNull.class);
      }
    }
    if (aMaxOccurs.compareTo (BigInteger.ONE) > 0)
    {
      if (!_hasAnnotation (aField, Size.class))
      {
        aField.annotate (Size.class).param ("min", aMinOccurs.intValue ()).param ("max", aMaxOccurs.intValue ());
      }
    }
    if (UNBOUNDED.equals (aMaxOccurs) && MathHelper.isGreaterThanZero (aMinOccurs))
    {
      if (!_hasAnnotation (aField, Size.class))
      {
        aField.annotate (Size.class).param ("min", aMinOccurs.intValue ());
      }
    }

    final XSTerm aTerm = aParticle.getTerm ();
    if (aTerm instanceof ElementDecl)
      _processElement (aField, (ElementDecl) aTerm);
    else
      if (aTerm instanceof DelayedRef.Element)
      {
        final XSElementDecl xsElementDecl = ((DelayedRef.Element) aTerm).get ();
        _processElement (aField, (ElementDecl) xsElementDecl);
      }
  }

  private void _processElement (final JFieldVar aField, final ElementDecl aElement)
  {
    final XSType elementType = aElement.getType ();

    if (elementType instanceof XSSimpleType)
      _processType ((XSSimpleType) elementType, aField);
    else
      if (elementType.getBaseType () instanceof XSSimpleType)
        _processType ((XSSimpleType) elementType.getBaseType (), aField);
  }

  private void _processType (final XSSimpleType aSimpleType, final JFieldVar aField)
  {
    if (aField.type ().name ().equals ("String"))
    {
      if (!_hasAnnotation (aField, Size.class))
      {
        final XSFacet fMaxLength = aSimpleType.getFacet ("maxLength");
        final Integer aMaxLength = fMaxLength == null ? null : StringParser.parseIntObj (fMaxLength.getValue ().value);
        final XSFacet fMinLength = aSimpleType.getFacet ("minLength");
        final Integer aMinLength = fMinLength == null ? null : StringParser.parseIntObj (fMinLength.getValue ().value);
        if (aMaxLength != null && aMinLength != null)
          aField.annotate (Size.class).param ("max", aMaxLength.intValue ()).param ("min", aMinLength.intValue ());
        else
          if (aMinLength != null)
            aField.annotate (Size.class).param ("min", aMinLength.intValue ());
          else
            if (aMaxLength != null)
              aField.annotate (Size.class).param ("max", aMaxLength.intValue ());
      }

      /**
       * <annox:annotate annox:class="javax.validation.constraints.Pattern"
       * message=
       * "Name can only contain capital letters, numbers and the symbols '-', '_', '/', ' '"
       * regexp="^[A-Z0-9_\s//-]*" />
       */
      if (aSimpleType.getFacet ("pattern") != null)
      {
        final String sPattern = aSimpleType.getFacet ("pattern").getValue ().value;
        // cxf-codegen fix
        if (!"\\c+".equals (sPattern))
        {
          if (!_hasAnnotation (aField, Pattern.class))
          {
            aField.annotate (Pattern.class).param ("regexp", sPattern);
          }
        }
      }
    }

    if (_isNumericType (aField))
    {
      final XSFacet aMaxInclusive = aSimpleType.getFacet ("maxInclusive");
      if (aMaxInclusive != null && _isValidValue (aMaxInclusive) && !_hasAnnotation (aField, DecimalMax.class))
      {
        aField.annotate (DecimalMax.class).param ("value", aMaxInclusive.getValue ().value);
      }

      final XSFacet aMinInclusive = aSimpleType.getFacet ("minInclusive");
      if (aMinInclusive != null && _isValidValue (aMinInclusive) && !_hasAnnotation (aField, DecimalMin.class))
      {
        aField.annotate (DecimalMin.class).param ("value", aMinInclusive.getValue ().value);
      }

      final XSFacet aMaxExclusive = aSimpleType.getFacet ("maxExclusive");
      if (aMaxExclusive != null && _isValidValue (aMaxExclusive) && !_hasAnnotation (aField, DecimalMax.class))
      {
        final JAnnotationUse aAnnotation = aField.annotate (DecimalMax.class);
        aAnnotation.param ("value", aMaxExclusive.getValue ().value);
        if (bJSR349)
        {
          aAnnotation.param ("inclusive", false);
        }
      }
      final XSFacet aMinExclusive = aSimpleType.getFacet ("minExclusive");
      if (aMinExclusive != null && _isValidValue (aMinExclusive) && !_hasAnnotation (aField, DecimalMin.class))
      {
        final JAnnotationUse aAnnotation = aField.annotate (DecimalMin.class);
        aAnnotation.param ("value", aMinExclusive.getValue ().value);
        if (bJSR349)
          aAnnotation.param ("inclusive", false);
      }
    }

    if (aSimpleType.getFacet ("totalDigits") != null)
    {
      final Integer totalDigits = aSimpleType.getFacet ("totalDigits") == null ? null
                                                                              : StringParser.parseIntObj (aSimpleType.getFacet ("totalDigits")
                                                                                                                     .getValue ().value);
      final Integer fractionDigits = aSimpleType.getFacet ("fractionDigits") == null ? null
                                                                                    : StringParser.parseIntObj (aSimpleType.getFacet ("fractionDigits")
                                                                                                                           .getValue ().value);
      if (!_hasAnnotation (aField, Digits.class) && (totalDigits != null || fractionDigits != null))
      {
        final JAnnotationUse annox = aField.annotate (Digits.class);
        if (totalDigits != null)
          if (fractionDigits != null)
            annox.param ("integer", totalDigits.intValue () - fractionDigits.intValue ());
          else
            annox.param ("integer", totalDigits.intValue ());
        if (fractionDigits != null)
          annox.param ("fraction", fractionDigits.intValue ());
      }
    }
  }

  /*
   * attribute from parent declaration
   */
  private void _processAttribute (final CValuePropertyInfo property, final ClassOutline clase)
  {
    final String propertyName = property.getName (false);

    final XSComponent definition = property.getSchemaComponent ();
    if (definition instanceof RestrictionSimpleTypeImpl)
    {
      final RestrictionSimpleTypeImpl particle = (RestrictionSimpleTypeImpl) definition;
      final XSSimpleType type = particle.asSimpleType ();
      final JFieldVar var = clase.implClass.fields ().get (propertyName);

      // if (particle.isRequired()) {
      // if (!hasAnnotation(var, NotNull.class)) {
      // if (notNullAnnotations) {
      // var.annotate(NotNull.class);
      // }
      // }
      // }

      _processType (type, var);
    }
  }

  /*
   * XS:Attribute
   */
  private void _processAttribute (final CAttributePropertyInfo property, final ClassOutline clase)
  {
    final String propertyName = property.getName (false);

    final XSComponent definition = property.getSchemaComponent ();
    final AttributeUseImpl particle = (AttributeUseImpl) definition;
    final XSSimpleType type = particle.getDecl ().getType ();

    final JFieldVar var = clase.implClass.fields ().get (propertyName);
    if (particle.isRequired ())
    {
      if (bNotNullAnnotations && !_hasAnnotation (var, NotNull.class))
      {
        var.annotate (NotNull.class);
      }
    }

    _processType (type, var);
  }

  private static boolean _isEqual (final long val, final String sValue)
  {
    return Long.toString (val).equals (sValue);
  }

  private static boolean _isValidValue (final XSFacet facet)
  {
    final String value = facet.getValue ().value;
    // cxf-codegen puts max and min as value when there is not anything defined
    // in wsdl.
    return value != null &&
           !(_isEqual (Long.MAX_VALUE, value) ||
             _isEqual (Integer.MAX_VALUE, value) ||
             _isEqual (Long.MIN_VALUE, value) || _isEqual (Integer.MIN_VALUE, value));
  }

  private static boolean _hasAnnotation (final JFieldVar aField, final Class <?> aAnnotationClass)
  {
    final Collection <JAnnotationUse> aAnnotations = aField.annotations ();
    if (aAnnotations != null)
    {
      final String sSearchName = aAnnotationClass.getCanonicalName ();
      for (final JAnnotationUse annotationUse : aAnnotations)
        if (annotationUse.getAnnotationClass ().fullName ().equals (sSearchName))
          return true;
    }
    return false;
  }

  private static boolean _isNumericType (@Nonnull final JFieldVar field)
  {
    for (final String type : NUMBER_TYPES)
      if (type.equalsIgnoreCase (field.type ().name ()))
        return true;

    try
    {
      final Class <?> aClass = Class.forName (field.type ().fullName ());
      return aClass != null && Number.class.isAssignableFrom (aClass);
    }
    catch (final Exception e)
    {
      // whatever
    }
    return false;
  }
}
