package controllers;

import dtos.DogDTO;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DogController {

    public static Map<String, DogDTO> dogDTOMap = new HashMap<>();

    public static void getAllDogs(Context ctx) {
        ctx.json(dogDTOMap.values());
    }

    public static void getDogById(Context ctx) {
        String id = ctx.pathParam("id");
        DogDTO dog = dogDTOMap.get(id);

        if (dog != null) {
            ctx.json(dog);
        } else {
            ctx.status(404).result("Dog not found");
        }
    }

    public static void createDog(Context ctx) {
        DogDTO newDog = ctx.bodyAsClass(DogDTO.class);
        // Generate a unique id using UUID
        String id = UUID.randomUUID().toString();

        newDog.setId(id);
        dogDTOMap.put(id, newDog);

        ctx.json(newDog);
    }

    public static void updateDog(Context ctx) {
        String id = ctx.pathParam("id");
        DogDTO updatedDog = ctx.bodyAsClass(DogDTO.class);

        if (dogDTOMap.containsKey(id)) {
            dogDTOMap.put(id, updatedDog);
        } else {
            ctx.status(404).result("Dog not found");
        }
    }

    public static void deleteDog(Context ctx) {
        String id = ctx.pathParam("id");

        if (dogDTOMap.containsKey(id)) {
            dogDTOMap.remove(id);
        } else {
            ctx.status(404).result("Dog not found");
        }
    }
}