package service;

import java.util.ArrayList;
import java.util.Arrays;

import org.openapitools.model.User;
import org.openapitools.model.Video;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private static final String USER_SERVICE_URL = "http://localhost:8081"; // URL del microservicio de usuarios

    private final RestTemplate restTemplate = new RestTemplate();

    public ArrayList<Video> getUserVideoHistory(String username) {
        try {
            // Construye la URL con el nombre de usuario como parámetro
            String url = String.format("%s/user/%s/history", USER_SERVICE_URL, username);

            // Envía una solicitud GET al microservicio de videos y obtiene un arreglo
            Video[] videosArray = restTemplate.getForObject(url, Video[].class);

            // Convierte el arreglo en un ArrayList para una mejor gestión
            return new ArrayList<>(Arrays.asList(videosArray));
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404
            return new ArrayList<>(); // Devuelve una lista vacía si ocurre un error
        }
    }

    public ArrayList<Long> getFollowingIds(String username) {
        try {
            // Construye la URL con el nombre de usuario como parámetro
            String url = String.format("%s/user/%s/following", USER_SERVICE_URL, username);

            // Envía una solicitud GET al microservicio de usuarios y obtiene un arreglo de IDs
            Long[] followingArray = restTemplate.getForObject(url, Long[].class);

            // Convierte el arreglo en un ArrayList para una mejor gestión
            return new ArrayList<>(Arrays.asList(followingArray));
        } catch (HttpClientErrorException e) {
            // Maneja errores como 400 o 404 devolviendo una lista vacía
            System.out.println("Error al obtener los usuarios seguidos: " + e.getStatusCode());
            return new ArrayList<>(); // Devuelve una lista vacía si ocurre un error
        } catch (Exception e) {
            // Manejo de otros errores no esperados
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<User> getFollowingProfiles(String username) {
        try {
            // Construye la URL con el nombre de usuario como parámetro
            String url = String.format("%s/user/%s/followingProfiles", USER_SERVICE_URL, username);

            // Envía una solicitud GET al microservicio de videos y obtiene un arreglo
            User[] usersArray = restTemplate.getForObject(url, User[].class);

            // Convierte el arreglo en un ArrayList para una mejor gestión
            return new ArrayList<>(Arrays.asList(usersArray));
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404
            return new ArrayList<>(); // Devuelve una lista vacía si ocurre un error
        }
    }

    public ArrayList<User> getNotFollowingProfiles(String username) {
        try {
            // Construye la URL con el nombre de usuario como parámetro
            String url = String.format("%s/user/%s/notfollowingProfiles", USER_SERVICE_URL, username);

            // Envía una solicitud GET al microservicio de videos y obtiene un arreglo
            User[] usersArray = restTemplate.getForObject(url, User[].class);

            // Convierte el arreglo en un ArrayList para una mejor gestión
            return new ArrayList<>(Arrays.asList(usersArray));
        } catch (HttpClientErrorException e) {
            // Maneja errores, como un 400 o 404
            return new ArrayList<>(); // Devuelve una lista vacía si ocurre un error
        }
    }
}
