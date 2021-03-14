package com.digitalinnovation.livecoding.controller;

import com.digitalinnovation.livecoding.document.Heroes;
import com.digitalinnovation.livecoding.repository.HeroesRepository;
import com.digitalinnovation.livecoding.service.HeroesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.digitalinnovation.livecoding.constants.HeroesConstant.HEROES_ENDPOINT_LOCAL;

@RestController
@Slf4j

public class HeroesController {
  HeroesService heroesService;

  HeroesRepository heroesRepository;

  private static final org.slf4j.Logger log =
    org.slf4j.LoggerFactory.getLogger(HeroesController.class);

  public HeroesController(HeroesService heroesService, HeroesRepository heroesRepository) {
    this.heroesService = heroesService;
    this.heroesRepository = heroesRepository;
  }

  @GetMapping(HEROES_ENDPOINT_LOCAL)
  @ResponseStatus(HttpStatus.OK)
  public Flux<Heroes> getAllItems() {
    log.info("requesting the list off all heroes");
    return heroesService.findAll();
  }

  @GetMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id) {
    log.info("Requesting the hero with id {}", id);
    return heroesService.findByIdHero(id)
      .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
      .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping(HEROES_ENDPOINT_LOCAL)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Heroes> createHero(@RequestBody Heroes heroes) {
    log.info("A new Hero was Created");
    return heroesService.save(heroes);
  }

  @DeleteMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<String>> deletebyIDHero(@PathVariable String id) {
    log.info("Deleting the hero with id {}", id);
    try {
      heroesService.deletebyIDHero(id);
      return Mono.just(new ResponseEntity<>("Hero deleted", HttpStatus.OK));
    }
    catch (EmptyResultDataAccessException e) {
      return Mono.just(new ResponseEntity<>("Hero not found", HttpStatus.NOT_FOUND));
    }
  }

  @PatchMapping(HEROES_ENDPOINT_LOCAL + "/{id}")
  public Mono<ResponseEntity<String>> updatebyIDHero(@PathVariable String id, @RequestBody Heroes heroes) {
    Mono<Heroes> heroMono = heroesService.findByIdHero(id);
    Heroes heroToUpdate = heroMono.block();
    if (heroToUpdate == null) {
      log.info("Hero not found");
      return Mono.just(new ResponseEntity<>("Hero not found", HttpStatus.NOT_FOUND));
    }
    else {
      log.info("Updating the Hero");
      if (heroes.getName() != null) {
        heroToUpdate.setName(heroes.getName());
      }
      if (heroes.getUniverse() != null) {
        heroToUpdate.setUniverse(heroes.getUniverse());
      }
      if (heroes.getFilms() != 0) {
        heroToUpdate.setFilms(heroes.getFilms());
      }
      heroesService.save(heroToUpdate);
      return Mono.just(new ResponseEntity<>("Hero updated", HttpStatus.OK));
    }
  }

}
