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
package org.springframework.samples.petclinic.customers.web;

import java.util.Date;

import org.hdiv.services.Mapper;
import org.hdiv.services.SecureIdentifiable;
import org.hdiv.services.TrustAssertion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetType;

import lombok.Data;

/**
 * @author mszarlinski@bravurasolutions.com on 2016-12-05.
 */
@Data
class PetDetails implements SecureIdentifiable<Integer>, Mapper<PetRequest> {

	@TrustAssertion(idFor = Pet.class)
	private Integer id;

	private String name;

	private String owner;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	private PetType type;

	PetDetails() {
	}

	PetDetails(final Pet pet) {
		id = pet.getId();
		name = pet.getName();
		owner = pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName();
		birthDate = pet.getBirthDate();
		type = pet.getType();
	}

	@Override
	public PetRequest map(final Class<PetRequest> arg0) {
		PetRequest request = new PetRequest();
		request.setId(id);
		request.setBirthDate(birthDate);
		request.setTypeId(type.getId());
		request.setName(name);
		return request;
	}
}
