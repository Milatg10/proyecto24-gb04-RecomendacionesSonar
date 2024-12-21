package org.openapitools.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

import org.openapitools.model.User;
import org.openapitools.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import service.UserService;
import service.VideoService;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-16T17:45:57.620163600+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
@Controller
@RequestMapping("${openapi.tubeFlixRecomendacionesYVisualizacionesOpenAPI30.base-path:}")
public class HomeApiController implements HomeApi {

    @Autowired
    private VideoService videoservice;

    @Autowired
    private UserService userservice;

    private final NativeWebRequest request;

    public HomeApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    // Método auxiliar para manejar respuestas estándar
    private <T> ResponseEntity<T> buildResponse(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    @Override
    public ResponseEntity<List<Video>> getVideosByGenre(String genre) {
        logMethodCall("getVideosByGenre");
        ArrayList<Video> videos = videoservice.getVideosByGenre(genre);
        return buildResponse(videos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Video>> searchVideosByTitle(String query) {
        logMethodCall("searchVideosByTitle");
        ArrayList<Video> videos = videoservice.getVideosByTitle(query);
        return buildResponse(videos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Video>> getUserVideoHistory(
            @NotNull @Parameter(name = "username", description = "Nombre del usuario", required = true, in = ParameterIn.QUERY) 
            @PathVariable("username") String username) {
        logMethodCall("getUserVideoHistory");
        ArrayList<Video> videoList = userservice.getUserVideoHistory(username);
        return buildResponse(videoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Video>> getTopVideos() {
        logMethodCall("getTopVideos");
        ArrayList<Video> videos = videoservice.getTopVideos();
        return buildResponse(videos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Video>> getRecommendationsByUsername(
            @NotNull @Parameter(name = "username", description = "Nombre del usuario", required = true, in = ParameterIn.PATH) 
            @PathVariable("username") String username) {
        logMethodCall("getRecommendationsByUsername");

        ArrayList<Video> videoList = userservice.getUserVideoHistory(username);

        if (videoList == null || videoList.isEmpty()) {
            return buildResponse(null, HttpStatus.NOT_FOUND);
        }

        String mostViewedGenre = getMostViewedGenre(videoList);

        if (mostViewedGenre == null) {
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<Video> videos = videoservice.getVideosByGenre(mostViewedGenre);

        return (videos == null || videos.isEmpty())
                ? buildResponse(null, HttpStatus.NOT_FOUND)
                : buildResponse(videos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Video>> getVideosByFollowing(@PathVariable("username") String username) {
        logMethodCall("getVideosByFollowing");

        ArrayList<Video> followingVideos = new ArrayList<>();
        ArrayList<Long> idFollowing = userservice.getFollowingIds(username);

        for (Long id : idFollowing) {
            followingVideos.addAll(videoservice.getRandomVideosByUserId(id));
        }

        return buildResponse(followingVideos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> getProfilesByFollowing(@PathVariable("username") String username) {
        logMethodCall("getProfilesByFollowing");
        ArrayList<User> followingProfiles = userservice.getFollowingProfiles(username);
        return buildResponse(followingProfiles, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<User>> getProfilesByNotFollowing(@PathVariable("username") String username) {
        logMethodCall("getProfilesByNotFollowing");
        ArrayList<User> notFollowingProfiles = userservice.getNotFollowingProfiles(username);
        return buildResponse(notFollowingProfiles, HttpStatus.OK);
    }

    // Método auxiliar para determinar el género más visto
    private String getMostViewedGenre(ArrayList<Video> videoList) {
        Map<String, Integer> genreCounts = new HashMap<>();

        for (Video video : videoList) {
            String genre = video.getGenre();
            if (genre != null) {
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
            }
        }

        return genreCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Método auxiliar para loguear llamadas a métodos
    private void logMethodCall(String methodName) {
        System.out.println("-------- HomeApiController -> " + methodName + "() -----------");
    }
}
