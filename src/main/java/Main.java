import controllers.DogController;
import dtos.DogDTO;
import io.javalin.Javalin;

import java.util.stream.Stream;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7007);
        // Endpoint routes for CRUD operations
        app.routes(() -> {
            path("/api/dogs", () -> {
                get(DogController::getAllDogs);
                // '{}' indicates that 'id' is a variable in the route
                get("/{id}", DogController::getDogById);
                post("/", DogController::createDog);
                put("/{id}", DogController::updateDog);
                delete("/{id}", DogController::deleteDog);
            });
        });

        // <--- Dogs --->
        Stream.of(
                new DogDTO("1", "Vovse", "Labrador", "Male", 3),
                new DogDTO("2", "Pølle", "Dachshund", "female", 10)
        ).forEach(dogDTO -> DogController.dogDTOMap.put(dogDTO.getId(), dogDTO));
    }
}