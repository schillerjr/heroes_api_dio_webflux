package com.digitalinnovation.livecoding;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.digitalinnovation.livecoding.document.Heroes;
import com.digitalinnovation.livecoding.repository.HeroesRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static com.digitalinnovation.livecoding.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;
import static org.springframework.http.RequestEntity.*;

@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@SpringBootTest
public class HeroesApiApplicationTests {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  HeroesRepository heroesRepository;

  @Test
  public void getAllHeroes(){

    webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/"))
            .exchange()
            .expectStatus().isOk()
            .expectBody();
  }

  @Test
  public void getOneHeroById(){

    webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"2")
      .exchange()
      .expectStatus().isOk()
      .expectBody();
  }

  @Test
  public void getOneHeroNotFound(){

    webTestClient.get().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"1")
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  public void createHero(){

    String body = "{\"id\": \"5\", \"name\": \"Thor\", \"universe\": \"marvel\", \"films\": 3}";

    webTestClient.post().uri(HEROES_ENDPOINT_LOCAL.concat("/"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchange()
            .expectStatus().isCreated();
  }

  @Test
  public void deleteHero(){

    webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"5")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
  }

  @Test
  public void deleteHeroNotFound(){

    webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"1")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  public void updateHero(){

    String body = "{\"films\": 3}";

    webTestClient.patch().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"2")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchange()
            .expectStatus().isOk();
  }

  @Test
  public void updateHeroNotFound(){

    String body = "{\"films\": 3}";

    webTestClient.patch().uri(HEROES_ENDPOINT_LOCAL.concat("/{id}"),"1")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(body))
            .exchange()
            .expectStatus().isNotFound();
  }

}
