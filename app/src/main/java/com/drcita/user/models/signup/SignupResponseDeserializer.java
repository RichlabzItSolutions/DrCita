package com.drcita.user.models.signup;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SignupResponseDeserializer implements JsonDeserializer<SignupResponse> {
    @Override
    public SignupResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SignupResponse response = new SignupResponse();
        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement errorsElement = jsonObject.get("errors");
        List<String> errorList = new ArrayList<>();

        if (errorsElement != null) {
            if (errorsElement.isJsonArray()) {
                for (JsonElement element : errorsElement.getAsJsonArray()) {
                    errorList.add(element.getAsString());
                }
            } else if (errorsElement.isJsonPrimitive()) {
                errorList.add(errorsElement.getAsString());
            }
        }

        response.setErrors(errorList);
        return response;
    }
}

