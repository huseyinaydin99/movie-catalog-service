package io.husobaba.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.husobaba.moviecatalogservice.model.CatalogItem;
import io.husobaba.moviecatalogservice.model.Movie;
import io.husobaba.moviecatalogservice.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {
    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    /*@HystrixCommand(
     fallbackMethod="getFallbackCatalogItem",
     threadPoolKey="movieInfoPool",
     threadPoolProperties={
      @HystrixProperty(name="coreSize",value = "20"),
      @HystrixProperty(name="maxQueueSize",value = "10")
    }
    )*/
//@HystrixProperty(name = "circuitBreaker.requestVolumeThresHold", value = "5"),
    //@HystrixProperty(name = "circuitBreaker.errorThresHoldPercentage", value = "50"),
    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),


            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
    })
    public CatalogItem getCatalogItem(Rating rating) {
        Movie movie = this.restTemplate.getForObject("http://localhost:8081/movie/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "Desc", rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
