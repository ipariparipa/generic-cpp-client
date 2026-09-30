/*
    This file is part of generic-cpp-client.

    sasCore is free software: you can redistribute it and/or modify
    it under the terms of the Lesser GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    sasCore is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with sasCore.  If not, see <http://www.gnu.org/licenses/>
 */

package generic_cpp_client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.meta.features.*;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.ModelUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.media.MediaType;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;

import java.util.*;

import static com.google.common.base.Strings.isNullOrEmpty;


import org.openapitools.codegen.languages.*;
import java.io.File;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.core.util.Json;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.tags.Tag;

public class GenericCppClientGenerator extends AbstractCppCodegen implements CodegenConfig
{
    private String sourceFolder = "src";
    private String apiVersion = "1.0.0";


    public static final String OUTPUT_MODE = "outputMode";
    public static final String CPP_NAMESPACE = "cppNamespace";
    public static final String DEFAULT_API_NAME = "defaultApiName";
    public static final String MODEL_PACKAGE = "modelPackage";
    public static final String API_PACKAGE = "apiPackage";
    public static final String FORCE_LONG_INTEGER = "forceLongInteger";

    private String outputMode = "header-only"; //"split";
    private String cppNamespace = "OA_api";
    private String defaultApiName = "";
    private boolean forceLongInteger = false;

    private final Set<String> parentModels = new HashSet<>();
    private final Multimap<String, CodegenModel> childrenByParent = ArrayListMultimap.create();

    public CodegenType getTag()
    {
        return CodegenType.CLIENT;
    }

    public String getName()
    {
        return "generic-cpp-client";
    }

    public String getHelp()
    {
        return "Generates a generic-cpp-client client library.";
    }

    public GenericCppClientGenerator()
    {
        super();

        outputFolder = "generated-code/generic-cpp-client";

        templateDir = "generic-cpp-client";

        apiPackage = "api";
        modelPackage = "model";

        modelTemplateFiles.put(
            "model.mustache",
            ".h"
        );

        apiTemplateFiles.put(
            "api.mustache",
            ".h"
        );

        languageSpecificPrimitives = new HashSet<>(
            Arrays.asList(
                "_types::integer",
                "_types::boolean",
                "_types::long_integer",
                "_types::floating",
                "_types::number"
            )
        );

        typeMapping = new HashMap<>();

        typeMapping.put("date", "_types::date");
        importMapping.put("_types::date","#include \"_types/date.h\"");
        typeMapping.put("DateTime", "_types::datetime");
        importMapping.put("_types::datetime","#include \"_types/date.h\"");
        typeMapping.put("string", "_types::string");
        importMapping.put("_types::string","#include \"_types/string.h\"");
        importMapping.put("_types::string_ref","#include \"_types/string.h\"");
        typeMapping.put("integer", "_types::integer");
        importMapping.put("_types::integer","#include \"_types/natives.h\"");
        typeMapping.put("int64", "_types::long_integer");
        typeMapping.put("long", "_types::long_integer");
        importMapping.put("_types::long_integer","#include \"_types/natives.h\"");
        typeMapping.put("boolean", "_types::boolean");
        importMapping.put("_types::boolean","#include \"_types/natives.h\"");
        typeMapping.put("array", "_types::array");
        importMapping.put("_types::array","#include \"_types/containers.h\"");
        importMapping.put("_types::array_ref","#include \"_types/containers.h\"");
        typeMapping.put("set", "_types::set");
        importMapping.put("_types::set","#include \"_types/containers.h\"");
        importMapping.put("_types::set_ref","#include \"_types/containers.h\"");
        typeMapping.put("map", "_types::map");
        importMapping.put("_types::map","#include \"_types/containers.h\"");
        importMapping.put("_types::map_ref","#include \"_types/containers.h\"");
        typeMapping.put("file", "_types::file");
        typeMapping.put("file_ref", "_types::file_ref");
        importMapping.put("_types::file","#include \"_types/file.h\"");
        importMapping.put("_types::file_ref","#include \"_types/file.h\"");
        typeMapping.put("object", "_types::object_ref");
        importMapping.put("_types::object_ref","#include \"_types/value.h\"");
        importMapping.put("_types::object","#include \"_types/value.h\"");
        typeMapping.put("value", "_types::value_ref");
        importMapping.put("_types::value_ref","#include \"_types/value.h\"");
        importMapping.put("_types::value","#include \"_types/value.h\"");
        typeMapping.put("binary", "_types::binary");
        importMapping.put("_types::binary","#include \"_types/binary.h\"");
        typeMapping.put("number", "_types::number");
        typeMapping.put("double", "_types::number");
        importMapping.put("_types::number","#include \"_types/natives.h\"");
        typeMapping.put("float", "_types::floating");
        importMapping.put("_types::floating","#include \"_types/natives.h\"");
        typeMapping.put("UUID", "_types::uuid");
        importMapping.put("_types::uuid","#include \"_types/uuid.h\"");
        importMapping.put("_types::uuid_ref","#include \"_types/uuid.h\"");
        typeMapping.put("URI", "_types::uri");
        importMapping.put("_types::uri","#include \"_types/uri.h\"");
        importMapping.put("_types::uri_ref","#include \"_types/uri.h\"");
        typeMapping.put("ByteArray", "_types::byte_array");
        importMapping.put("_types::byte_array","#include \"_types/byte_array.h\"");
        importMapping.put("_types::byte_array_ref","#include \"_types/byte_array.h\"");
        typeMapping.put("AnyType", "_types::value_ref");
        importMapping.put("_types::value","#include \"_types/value.h\"");
        importMapping.put("_types::value_ref","#include \"_types/value.h\"");

        cliOptions.add(CliOption.newString(
            OUTPUT_MODE,
            "C++ output mode: split or header-only"
            ).defaultValue(outputMode));

        cliOptions.add(CliOption.newString(
            CPP_NAMESPACE,
            "the main namespace in the generated C++ code"
            ).defaultValue(cppNamespace));

        cliOptions.add(CliOption.newString(
            DEFAULT_API_NAME,
            "overrides the `Default` name"
            ).defaultValue(defaultApiName));

        cliOptions.add(CliOption.newString(
            MODEL_PACKAGE,
            "overrides the `model` name"
            ).defaultValue(modelPackage));

        cliOptions.add(CliOption.newString(
            API_PACKAGE,
            "overrides the `api` name"
            ).defaultValue(apiPackage));


        cliOptions.add(CliOption.newString(
            FORCE_LONG_INTEGER,
            "overrides all integer to long integer"
            ).defaultValue("false"));

        apiNameSuffix = "";
    }

    @Override
    public String toModelImport(String name)
    {
        if (importMapping.containsKey(name))
            return importMapping.get(name);
        else
            return "#include \"../" + modelPackage + "/" + toModelFilename(name) + ".h\"";
    }

    @Override
    public CodegenModel fromModel(String name, Schema model)
    {

        CodegenModel codegenModel = super.fromModel(name, model);

        for(String n : codegenModel.allOf)
            codegenModel.imports.add(n);

        if(!isNullOrEmpty(codegenModel.dataType) && codegenModel.oneOf.isEmpty() && codegenModel.anyOf.isEmpty())
            codegenModel.imports.add(codegenModel.dataType);

        Schema addlProps = ModelUtils.getAdditionalProperties(model);
        if (addlProps != null && ModelUtils.hasProperties(model))
        {
            codegenModel.additionalPropertiesType = getTypeDeclaration(addlProps);
            codegenModel.imports.add("_types::value");
        }

        return codegenModel;
    }

    @Override
    public CodegenProperty fromProperty(String name, Schema schema, boolean required)
    {
        if (forceLongInteger && "integer".equals(schema.getType()))
            schema.setFormat("int64");

        return super.fromProperty(name, schema, required);
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, List<Server> servers)
    {
        CodegenOperation codegenOperation = super.fromOperation(path, httpMethod, operation, servers);

        if (operation.getResponses() != null && !operation.getResponses().isEmpty())
        {
            ApiResponse methodResponse = findMethodResponse(operation.getResponses());

            if (methodResponse != null)
            {
                Schema response = ModelUtils.getSchemaFromResponse(openAPI, methodResponse);
                response = unaliasSchema(response);
                if (response != null)
                {
                    CodegenProperty cm = fromProperty("response", response, false);
                    codegenOperation.vendorExtensions.put("codegenResponse", cm);
                    if ("_types::http_content_ref".equals(cm.dataType))
                        codegenOperation.vendorExtensions.put("codegenResponse-isHttpContent", true);
                }
            }
        }

        if (operation.getRequestBody() == null
            || operation.getRequestBody().getContent() == null
            || codegenOperation.bodyParam == null)
            return codegenOperation;

        List<Map<String, String>> bodyTypes = new ArrayList<>();

        for (Map.Entry<String, MediaType> entry : operation.getRequestBody().getContent().entrySet())
        {
            CodegenProperty property = fromProperty("body", entry.getValue().getSchema());

            Map<String, String> item = new HashMap<>();
            item.put("mediaType", entry.getKey());
            item.put("dataType", property.dataType);

            bodyTypes.add(item);
        }

        codegenOperation.bodyParam.vendorExtensions.put("bodyTypes", bodyTypes);

        return codegenOperation;
    }

    @Override
    public String toApiName(String name)
    {
        if ((isNullOrEmpty(name) || "Default".equals(name)) && !isNullOrEmpty(defaultApiName))
            name = defaultApiName;

        return super.toApiName(name);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels)
    {
        OperationMap operations = objs.getOperations();
        List<CodegenOperation> operationList = operations.getOperation();
        for (CodegenOperation op : operationList)
        {
            for (String hdr : op.imports)
            {
                if (importMapping.containsKey(hdr))
                    continue;

                operations.put("hasModelImport", true);
                break;
            }
        }

        OperationsMap result = super.postProcessOperationsWithModels(objs, allModels);

        for (CodegenOperation op : result.getOperations().getOperation())
        {
            int pos = op.path.indexOf('{');

            op.vendorExtensions.put(
                "cleanPath",
                pos >= 0 ? op.path.substring(0, pos) : op.path
            );
        }

        for (CodegenOperation operation : result.getOperations().getOperation())
        {
            for (CodegenResponse response : operation.responses)
            {
                List<Map<String, String>> mediaTypes = new ArrayList<>();

                if (response.getContent() != null)
                {
                    for (Map.Entry<String, CodegenMediaType> entry : response.getContent().entrySet())
                    {
                        Map<String, String> item = new HashMap<>();
                        item.put("mediaType", entry.getKey());
                        mediaTypes.add(item);

                        CodegenProperty schema = entry.getValue().getSchema();
                        if (schema != null && schema.dataType != null)
                            item.put("dataType", schema.dataType);
                    }
                }

                response.vendorExtensions.put("responseTypes", mediaTypes);
            }
        }

        return result;
    }

    protected boolean isFileSchema(CodegenProperty property)
    {
        return property.baseType.equals("_types::http_content_ref");
    }

    @Override
    public String toModelFilename(String name)
    {
        return toModelName(name);
    }

    @Override
    public String toApiFilename(String name)
    {
        return toApiName(name);
    }

    private Schema resolveSchema(Schema schema)
    {
        if (schema == null)
            return null;

        if (StringUtils.isNotEmpty(schema.get$ref()))
        {
            String ref = ModelUtils.getSimpleRef(schema.get$ref());
            Schema resolved = ModelUtils.getSchema(openAPI, ref);
            return resolved != null ? resolved : schema;
        }

        return schema;
    }

    private boolean isPureMapSchema(Schema schema)
    {
        if (!ModelUtils.isMapSchema(schema))
            return false;

        return schema.getProperties() == null || schema.getProperties().isEmpty();
    }

    @Override
    public String getTypeDeclaration(String name)
    {
        return modelPackage + "::" + name + "_ref";
    }

    @Override
    public String getTypeDeclaration(Schema p)
    {
        Schema resolved = resolveSchema(p);

        if (ModelUtils.isArraySchema(resolved))
        {
            Schema inner = ModelUtils.getSchemaItems(resolved);
            return getSchemaType(p) + "<" + getTypeDeclaration(inner) + ">";
        } else if (isPureMapSchema(resolved)) {
            // Only treat as map if it has additionalProperties but NO defined properties
            Schema inner = ModelUtils.getAdditionalProperties(resolved);
            // inner can be null if additionalProperties is a boolean or not present
            String innerType = inner != null ? getTypeDeclaration(inner) : "_types::object_ref";
            return getSchemaType(p) + "<_types::string, " + innerType + ">";
        }

        String openAPIType = getSchemaType(p);

        if (ModelUtils.isStringSchema(p)
                || ModelUtils.isDateSchema(p) || ModelUtils.isDateTimeSchema(p)
                || ModelUtils.isFileSchema(p) || ModelUtils.isUUIDSchema(p)
                || languageSpecificPrimitives.contains(openAPIType))
            return toModelName(openAPIType);
        else if (ModelUtils.isObjectSchema(p))
            return "_types::object_ref";
        else if(typeMapping.containsKey(super.getSchemaType(p)))
            return openAPIType;

        return modelPackage + "::" + openAPIType + "_ref";
    }

    @Override
    public String getSchemaType(Schema p)
    {
        String openAPIType = super.getSchemaType(p);
        String type = null;

        if (typeMapping.containsKey(openAPIType))
            type = typeMapping.get(openAPIType);
        else
            type = openAPIType;

        return toModelName(type);
    }

    @Override
    public String toDefaultValue(Schema p)
    {
        String type_name = null;
        String openAPIType = getSchemaType(p);
        if (ModelUtils.isFileSchema(p) || ModelUtils.isBinarySchema(p))
            return modelPackage + "::" + openAPIType + "_ref";
        else if (ModelUtils.isStringSchema(p)
                || ModelUtils.isDateSchema(p) || ModelUtils.isDateTimeSchema(p)
                || ModelUtils.isFileSchema(p) || ModelUtils.isUUIDSchema(p)
                || languageSpecificPrimitives.contains(openAPIType))
            type_name = toModelName(openAPIType);
        else if (ModelUtils.isObjectSchema(p))
            type_name = "_types::object_ref";
        else if(typeMapping.containsKey(super.getSchemaType(p)))
            type_name = openAPIType;
        else
            type_name = modelPackage + "::" + openAPIType + "_ref";

        return "_types::default_value<" + type_name + ">()";
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs)
    {
        objs = super.postProcessModels(objs);

        for (ModelMap modelMap : objs.getModels())
        {
            CodegenModel model = modelMap.getModel();

            if (model.getComposedSchemas() == null)
                continue;

            assignAlternativeNames(model.getComposedSchemas().getOneOf());
            assignAlternativeNames(model.getComposedSchemas().getAnyOf());
        }

        return objs;
    }

    private void assignAlternativeNames(List<CodegenProperty> alternatives)
    {
        if (alternatives == null)
            return;

        Map<String, Integer> counts = new HashMap<>();

        for (CodegenProperty property : alternatives)
        {
            String type = property.getOpenApiType();

            if (type == null || type.isEmpty())
                type = "Type";

            String name = Character.toUpperCase(type.charAt(0)) + type.substring(1);

            if (property.isEnum)
            {
                List<Map<String, String>> enumVars = new ArrayList<>();

                if (property.allowableValues != null)
                {
                    Object values = property.allowableValues.get("values");

                    if (values instanceof List<?>)
                    {
                        List<?> list = (List<?>) values;

                        for (Object value : list)
                        {
                            String originalValue = String.valueOf(value);

                            Map<String, String> enumVar = new HashMap<>();
                            enumVar.put("name", toEnumVarName(originalValue, null));
                            enumVar.put("value", originalValue);

                            enumVars.add(enumVar);
                        }
                    }
                }

                property.vendorExtensions.put("enumVars", enumVars);

                name += "Enum";
            }
            else if (property.isMap)
                name += "Map";
            else if (property.isArray)
                name += "Array";

            int count = counts.merge(name, 1, Integer::sum);

            if (count > 1)
                name += "_" + count;

            property.vendorExtensions.put("alternativeName", name);
        }
    }

    @Override
    public void processOpts()
    {
        super.processOpts();

        outputMode = additionalProperties.getOrDefault(OUTPUT_MODE, outputMode).toString();

        if (!outputMode.equals("split") && !outputMode.equals("header-only"))
            throw new IllegalArgumentException("Invalid outputMode: " + outputMode);

        additionalProperties.put(OUTPUT_MODE, outputMode);
        additionalProperties.put("headerOnly",outputMode.equals("header-only"));

        cppNamespace = additionalProperties.getOrDefault(CPP_NAMESPACE, cppNamespace).toString();
        additionalProperties.put(CPP_NAMESPACE, cppNamespace);

        defaultApiName = additionalProperties.getOrDefault(DEFAULT_API_NAME, defaultApiName).toString();

        modelPackage = additionalProperties.getOrDefault(MODEL_PACKAGE, modelPackage).toString();
        apiPackage = additionalProperties.getOrDefault(API_PACKAGE, apiPackage).toString();

        if (additionalProperties.containsKey(FORCE_LONG_INTEGER))
            forceLongInteger = Boolean.parseBoolean(additionalProperties.get(FORCE_LONG_INTEGER).toString());
    }

    @Override
    public String escapeReservedWord(String name)
    {
        return "_" + name;
    }

    public String modelFileFolder()
    {
        return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar);
    }

    @Override
    public String apiFileFolder()
    {
        return outputFolder + "/" + sourceFolder + "/" + apiPackage().replace('.', File.separatorChar);
    }
}
