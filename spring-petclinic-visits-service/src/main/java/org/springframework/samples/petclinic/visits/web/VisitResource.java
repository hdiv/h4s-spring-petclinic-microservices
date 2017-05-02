/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.visits.web;

import java.util.List;

import javax.validation.Valid;

import org.hdiv.services.TrustAssertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.visits.model.Visit;
import org.springframework.samples.petclinic.visits.model.VisitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Maciej Szarlinski
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class VisitResource {

	private interface Pet {

	}

	private final VisitRepository visitRepository;

	@PostMapping("owners/*/pets/{petId}/visits")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void create(@Valid @RequestBody final Visit visit, @TrustAssertion(idFor = Pet.class) @PathVariable("petId") final int petId) {

		visit.setPetId(petId);
		log.info("Saving visit {}", visit);
		visitRepository.save(visit);
	}

	@GetMapping("owners/*/pets/{petId}/visits")
	public List<Visit> visits(@PathVariable("petId") final int petId) {
		return visitRepository.findByPetId(petId);
	}
}
