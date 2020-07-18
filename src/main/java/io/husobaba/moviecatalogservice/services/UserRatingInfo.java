package io.husobaba.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.husobaba.moviecatalogservice.model.Rating;
import io.husobaba.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserRatingInfo {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;
//@HystrixProperty(name="circuitBreaker.requestVolumeThresHold",value="5"),
    // @HystrixProperty(name="circuitBreaker.errorThresHoldPercentage",value="50"),
    @HystrixCommand(fallbackMethod="getFallbackUserRating",commandProperties={
    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="2000"),


    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="5000")
    })
    public UserRating getUserRating(@PathVariable("userId") String userId){
        return restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
    }

    public UserRating getFallbackUserRating(@PathVariable("userId") String userId){
        UserRating userRating = new UserRating();
        userRating.setUserRating(Arrays.asList(new Rating("User rating not found",0)));
        return userRating;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
