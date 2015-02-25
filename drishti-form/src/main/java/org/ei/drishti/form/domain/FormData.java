package org.ei.drishti.form.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormData {
    @JsonProperty
    private String bind_type;
    @JsonProperty
    private String default_bind_path;
    @JsonProperty
    private List<FormField> fields;
    @JsonProperty
    private List<SubFormData> sub_forms;

    private Map<String, String> mapOfFieldsByName;

    public FormData() {
    }

    public FormData(String bind_type, String default_bind_path, List<FormField> fields, List<SubFormData> sub_forms) {
        this.bind_type = bind_type;
        this.default_bind_path = default_bind_path;
        this.fields = fields;
        this.sub_forms = sub_forms;
    }

    public List<FormField> fields() {
        return fields;
    }

    public String getField(String name) {
        if (mapOfFieldsByName == null) {
            createFieldMapByName();
        }
        return mapOfFieldsByName.get(name);
    }

    private void createFieldMapByName() {
        mapOfFieldsByName = new HashMap<>();
        for (FormField field : fields) {
            mapOfFieldsByName.put(field.name(), field.value());
        }
    }

    public SubFormData getSubFormByName(String name) {
        for (SubFormData sub_form : sub_forms) {
            if (StringUtils.equalsIgnoreCase(name, sub_form.name()))
                return sub_form;
        }
        throw new RuntimeException(MessageFormat.format("No sub form with the given name: {0}, in formData: {1}", name, this));
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
