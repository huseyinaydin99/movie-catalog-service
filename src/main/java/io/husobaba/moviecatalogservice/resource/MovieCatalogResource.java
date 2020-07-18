package io.husobaba.moviecatalogservice.resource;

import com.netflix.ribbon.proxy.annotation.Hystrix;
import io.husobaba.moviecatalogservice.model.CatalogItem;
import io.husobaba.moviecatalogservice.model.Movie;
import io.husobaba.moviecatalogservice.model.Rating;
import io.husobaba.moviecatalogservice.model.UserRating;
import io.husobaba.moviecatalogservice.services.MovieInfo;
import io.husobaba.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    @Qualifier("webClientBuilder")
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        //RestTemplate restTemplate = new RestTemplate();
        //Movie movie = restTemplate.getForObject("http://localhost:8081/movies/foo", Movie.class);

        //http://localhost:8083/ratingsdata/users/
        UserRating ratings = userRatingInfo.getUserRating(userId);

        /*ratings.getUserRating().stream().map(rating -> {
            Movie movie = this.restTemplate.getForObject("http://localhost:8081/movie/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());*/

        List<CatalogItem> catalogItemList = ratings.getUserRating().stream().map(new Function<Rating, CatalogItem>() {
            @Override
            public CatalogItem apply(Rating rating) {
                //Movie movie = restTemplate.getForObject("http://localhost:8081/movie/" + rating.getMovieId(), Movie.class);
                //WebClient webClient = webClientBuilder.build();
                //Movie movie = webClient.get().uri("http://localhost:8081/movie/" + rating.getMovieId()).retrieve().bodyToMono(Movie.class).block();
                //Movie movie2 = webClientBuilder.build().get().uri("http://localhost:8081/movie/" + rating.getMovieId()).retrieve().bodyToMono(Movie.class).block();

                //return new CatalogItem(movie.getName(), "Desc", rating.getRating());
                return movieInfo.getCatalogItem(rating);
            }
        }).collect(Collectors.toList());
        return catalogItemList;
       /* return ratings.stream().map(new Function<Rating, CatalogItem>() {
            @Override
            public CatalogItem apply(Rating rating) {
                Movie movie = restTemplate.getForObject("http://localhost:8081/movie/" + rating.getMovieId(), Movie.class);
                return new CatalogItem(movie.getName(),"Desc",rating.getRating());
            }
        }).collect(Collectors.toList());*/
        //return Collections.singletonList(new CatalogItem("Recep İvedik","Test",Integer.parseInt(userId)));
        /*List<CatalogItem> catalogItemList = Arrays.asList(new CatalogItem("Gora","Test",Integer.parseInt(userId)));
        return catalogItemList;*/
    }



    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WebClient.Builder getWebClientBuilder() {
        return webClientBuilder;
    }

    public void setWebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
}
