package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.SubItem;
import org.flowant.website.repository.SubItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class PopularSubItemRest extends RepositoryRest<SubItem, UUID, SubItemRepository> {

    public final static String SUBITEM = "/popular_sub_item";

    @PostMapping(value = SUBITEM)
    public Mono<ResponseEntity<SubItem>> post(@Valid @RequestBody SubItem subItem) {
        return super.post(subItem);
    }

    @PutMapping(value = SUBITEM)
    public Mono<ResponseEntity<SubItem>> put(@Valid @RequestBody SubItem subItem) {
        return super.put(subItem);
    }

    @GetMapping(value = SUBITEM + PATH_SEG_ID)
    public Mono<ResponseEntity<SubItem>> getById(@PathVariable(value = ID) String id) {
        return super.getById(UUID.fromString(id));
    }

    @DeleteMapping(value = SUBITEM + PATH_SEG_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(UUID.fromString(id));
    }

}
