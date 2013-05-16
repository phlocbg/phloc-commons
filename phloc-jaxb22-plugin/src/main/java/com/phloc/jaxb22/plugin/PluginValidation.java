package com.phloc.jaxb22.plugin;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.xml.sax.ErrorHandler;

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
 */
public class PluginValidation extends Plugin {
  public static final String PLUGIN_OPTION_NAME = "XJsr303Annotations";
  public static final String TARGET_NAMESPACE_PARAMETER_NAME = PLUGIN_OPTION_NAME + ":targetNamespace";
  public static final String JSR_349 = PLUGIN_OPTION_NAME + ":JSR_349";
  public static final String GENERATE_NOT_NULL_ANNOTATIONS = PLUGIN_OPTION_NAME + ":generateNotNullAnnotations";
  private static final BigInteger UNBOUNDED = BigInteger.valueOf (XSParticle.UNBOUNDED);

  public static final String [] NUMBERS = new String [] { "BigDecimal",
                                                         "BigInteger",
                                                         "String",
                                                         "byte",
                                                         "short",
                                                         "int",
                                                         "long" };

  private final String namespace = "http://jaxb.dev.java.net/plugin/code-injector";
  private String targetNamespace = "TARGET_NAMESPACE";
  private boolean bJSR349 = false;
  private boolean bNotNullAnnotations = true;

  @Override
  public String getOptionName () {
    return PLUGIN_OPTION_NAME;
  }

  @Override
  public int parseArgument (final Options opt, final String [] args, final int i) throws BadCommandLineException,
                                                                                 IOException {
    final String sArg = args[i];
    int nConsumed = 0;
    final int indexOfNamespace = sArg.indexOf (TARGET_NAMESPACE_PARAMETER_NAME);
    if (indexOfNamespace > 0) {
      targetNamespace = sArg.substring (indexOfNamespace + TARGET_NAMESPACE_PARAMETER_NAME.length () + "=".length ());
      nConsumed++;
    }

    final int index = sArg.indexOf (JSR_349);
    if (index > 0) {
      bJSR349 = Boolean.parseBoolean (sArg.substring (index + JSR_349.length () + "=".length ()));
      nConsumed++;
    }

    final int index_generateNotNullAnnotations = sArg.indexOf (GENERATE_NOT_NULL_ANNOTATIONS);
    if (index_generateNotNullAnnotations > 0) {
      bNotNullAnnotations = Boolean.parseBoolean (sArg.substring (index_generateNotNullAnnotations +
                                                                  GENERATE_NOT_NULL_ANNOTATIONS.length () +
                                                                  "=".length ()));
      nConsumed++;
    }

    return nConsumed;
  }

  @Override
  public List <String> getCustomizationURIs () {
    return Collections.singletonList (namespace);
  }

  @Override
  public boolean isCustomizationTagName (final String nsUri, final String localName) {
    return nsUri.equals (namespace) && localName.equals ("code");
  }

  @Override
  public void onActivated (final Options opts) throws BadCommandLineException {
    super.onActivated (opts);
  }

  @Override
  public String getUsage () {
    return "  -XJsr303Annotations      :  inject Bean validation annotations (JSR 303); -XJsr303Annotations:targetNamespace=http://www.foo.com/bar  :      additional settings for @Valid annotation";
  }

  @Override
  public boolean run (final Outline model, final Options opt, final ErrorHandler errorHandler) {
    try {
      for (final ClassOutline co : model.getClasses ()) {
        final List <CPropertyInfo> properties = co.target.getProperties ();
        for (final CPropertyInfo property : properties) {
          if (property instanceof CElementPropertyInfo) {
            _processElement ((CElementPropertyInfo) property, co);
          }
          else
            if (property instanceof CAttributePropertyInfo) {
              _processAttribute ((CAttributePropertyInfo) property, co);
            }
            else
              if (property instanceof CValuePropertyInfo) {
                _processAttribute ((CValuePropertyInfo) property, co);
              }
        }
      }

      return true;
    }
    catch (final Exception e) {
      e.printStackTrace ();
      return false;
    }
  }

  /*
   * XS:Element
   */
  private void _processElement (final CElementPropertyInfo property, final ClassOutline classOutline) {
    final XSComponent schemaComponent = property.getSchemaComponent ();
    final ParticleImpl particle = (ParticleImpl) schemaComponent;
    final BigInteger minOccurs = particle.getMinOccurs ();
    final BigInteger maxOccurs = particle.getMaxOccurs ();
    final JFieldVar field = classOutline.implClass.fields ().get (_getInternalName (property));

    // workaround for choices
    final boolean bRequired = property.isRequired ();
    if (MathHelper.isLowerThanZero (minOccurs) || minOccurs.compareTo (BigInteger.ONE) >= 0 && bRequired) {
      if (!_hasAnnotation (field, NotNull.class)) {
        if (bNotNullAnnotations) {
          field.annotate (NotNull.class);
        }
      }
    }
    if (maxOccurs.compareTo (BigInteger.ONE) > 0) {
      if (!_hasAnnotation (field, Size.class)) {
        field.annotate (Size.class).param ("min", minOccurs.longValue ()).param ("max", maxOccurs.longValue ());
      }
    }
    if (UNBOUNDED.equals (maxOccurs) && MathHelper.isGreaterThanZero (minOccurs)) {
      if (!_hasAnnotation (field, Size.class)) {
        field.annotate (Size.class).param ("min", minOccurs.longValue ());
      }
    }

    final XSTerm term = particle.getTerm ();
    if (term instanceof ElementDecl) {
      _processElement (field, (ElementDecl) term);
    }
    else
      if (term instanceof DelayedRef.Element) {
        final XSElementDecl xsElementDecl = ((DelayedRef.Element) term).get ();
        _processElement (field, (ElementDecl) xsElementDecl);
      }

  }

  private void _processElement (final JFieldVar var, final ElementDecl element) {
    final XSType elementType = element.getType ();

    _validAnnotation (elementType, var);

    if (elementType instanceof XSSimpleType) {
      _processType ((XSSimpleType) elementType, var);
    }
    else
      if (elementType.getBaseType () instanceof XSSimpleType) {
        _processType ((XSSimpleType) elementType.getBaseType (), var);
      }
  }

  private void _validAnnotation (final XSType elementType, final JFieldVar var) {
    if (elementType.getTargetNamespace ().startsWith (targetNamespace) && elementType.isComplexType ()) {
      if (!_hasAnnotation (var, Valid.class)) {
        var.annotate (Valid.class);
      }
    }
  }

  private void _processType (final XSSimpleType simpleType, final JFieldVar field) {
    if (!_hasAnnotation (field, Size.class) && field.type ().name ().equals ("String")) {
      final XSFacet fMaxLength = simpleType.getFacet ("maxLength");
      final Integer aMaxLength = fMaxLength == null ? null : StringParser.parseIntObj (fMaxLength.getValue ().value);
      final XSFacet fMinLength = simpleType.getFacet ("minLength");
      final Integer aMinLength = fMinLength == null ? null : StringParser.parseIntObj (fMinLength.getValue ().value);
      if (aMaxLength != null && aMinLength != null)
        field.annotate (Size.class).param ("max", aMaxLength.intValue ()).param ("min", aMinLength.intValue ());
      else
        if (aMinLength != null)
          field.annotate (Size.class).param ("min", aMinLength.intValue ());
        else
          if (aMaxLength != null)
            field.annotate (Size.class).param ("max", aMaxLength.intValue ());
    }

    final XSFacet maxInclusive = simpleType.getFacet ("maxInclusive");
    if (maxInclusive != null &&
        _isNumericType (field) &&
        _isValidValue (maxInclusive) &&
        !_hasAnnotation (field, DecimalMax.class)) {
      field.annotate (DecimalMax.class).param ("value", maxInclusive.getValue ().value);
    }

    final XSFacet minInclusive = simpleType.getFacet ("minInclusive");
    if (minInclusive != null &&
        _isNumericType (field) &&
        _isValidValue (minInclusive) &&
        !_hasAnnotation (field, DecimalMin.class)) {
      field.annotate (DecimalMin.class).param ("value", minInclusive.getValue ().value);
    }

    final XSFacet maxExclusive = simpleType.getFacet ("maxExclusive");
    if (maxExclusive != null &&
        _isNumericType (field) &&
        _isValidValue (maxExclusive) &&
        !_hasAnnotation (field, DecimalMax.class)) {
      final JAnnotationUse annotate = field.annotate (DecimalMax.class);
      annotate.param ("value", maxExclusive.getValue ().value);
      if (bJSR349) {
        annotate.param ("inclusive", false);
      }
    }
    final XSFacet minExclusive = simpleType.getFacet ("minExclusive");
    if (minExclusive != null &&
        _isNumericType (field) &&
        _isValidValue (minExclusive) &&
        !_hasAnnotation (field, DecimalMin.class)) {
      final JAnnotationUse annotate = field.annotate (DecimalMin.class);
      annotate.param ("value", minExclusive.getValue ().value);
      if (bJSR349)
        annotate.param ("inclusive", false);
    }

    if (simpleType.getFacet ("totalDigits") != null) {
      final Integer totalDigits = simpleType.getFacet ("totalDigits") == null
                                                                             ? null
                                                                             : StringParser.parseIntObj (simpleType.getFacet ("totalDigits")
                                                                                                                   .getValue ().value);
      final Integer fractionDigits = simpleType.getFacet ("fractionDigits") == null
                                                                                   ? null
                                                                                   : StringParser.parseIntObj (simpleType.getFacet ("fractionDigits")
                                                                                                                         .getValue ().value);
      if (!_hasAnnotation (field, Digits.class) && (totalDigits != null || fractionDigits != null)) {
        final JAnnotationUse annox = field.annotate (Digits.class);
        if (totalDigits != null)
          if (fractionDigits != null)
            annox.param ("integer", totalDigits.intValue () - fractionDigits.intValue ());
          else
            annox.param ("integer", totalDigits.intValue ());
        if (fractionDigits != null)
          annox.param ("fraction", fractionDigits.intValue ());
      }
    }
    /**
     * <annox:annotate annox:class="javax.validation.constraints.Pattern"
     * message=
     * "Name can only contain capital letters, numbers and the symbols '-', '_', '/', ' '"
     * regexp="^[A-Z0-9_\s//-]*" />
     */
    if (simpleType.getFacet ("pattern") != null) {
      final String pattern = simpleType.getFacet ("pattern").getValue ().value;
      if ("String".equals (field.type ().name ())) {
        // cxf-codegen fix
        if (!"\\c+".equals (pattern)) {
          if (!_hasAnnotation (field, Pattern.class)) {
            field.annotate (Pattern.class).param ("regexp", pattern);
          }
        }
      }
    }
  }

  /*
   * attribute from parent declaration
   */
  private void _processAttribute (final CValuePropertyInfo property, final ClassOutline clase) {
    final String propertyName = property.getName (false);

    final XSComponent definition = property.getSchemaComponent ();
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

    _validAnnotation (type, var);
    _processType (type, var);
  }

  /*
   * XS:Attribute
   */
  private void _processAttribute (final CAttributePropertyInfo property, final ClassOutline clase) {
    final String propertyName = property.getName (false);

    final XSComponent definition = property.getSchemaComponent ();
    final AttributeUseImpl particle = (AttributeUseImpl) definition;
    final XSSimpleType type = particle.getDecl ().getType ();

    final JFieldVar var = clase.implClass.fields ().get (propertyName);
    if (particle.isRequired ()) {
      if (!_hasAnnotation (var, NotNull.class)) {
        if (bNotNullAnnotations) {
          var.annotate (NotNull.class);
        }
      }
    }

    _validAnnotation (type, var);
    _processType (type, var);
  }

  private static boolean _isEqual (final long val, final String sValue) {
    return Long.toString (val).equals (sValue);
  }

  private static boolean _isValidValue (final XSFacet facet) {
    final String value = facet.getValue ().value;
    // cxf-codegen puts max and min as value when there is not anything defined
    // in wsdl.
    return value != null &&
           !(_isEqual (Long.MAX_VALUE, value) ||
             _isEqual (Integer.MAX_VALUE, value) ||
             _isEqual (Long.MIN_VALUE, value) || _isEqual (Integer.MIN_VALUE, value));
  }

  private static boolean _hasAnnotation (final JFieldVar var, final Class <?> annotationClass) {
    final Collection <JAnnotationUse> aAnnotations = var.annotations ();
    if (aAnnotations != null) {
      final String sSearchName = annotationClass.getCanonicalName ();
      for (final JAnnotationUse annotationUse : aAnnotations)
        if (annotationUse.getAnnotationClass ().fullName ().equals (sSearchName))
          return true;
    }
    return false;
  }

  private static String _getInternalName (final CElementPropertyInfo property) {
    return property.getName (false);
  }

  private static boolean _isNumericType (@Nonnull final JFieldVar field) {
    for (final String type : NUMBERS)
      if (type.equalsIgnoreCase (field.type ().name ()))
        return true;

    try {
      Class <?> aClass = Class.forName (field.type ().fullName ());
      while (aClass.getSuperclass () != Object.class) {
        if (aClass.getSuperclass () == Number.class)
          return true;
        aClass = aClass.getSuperclass ();
      }
    }
    catch (final Exception e) {
      // whatever
    }
    return false;
  }
}
