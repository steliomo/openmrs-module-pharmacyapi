/*
 * Friends in Global Health - FGH © 2017
 */
package org.openmrs.module.pharmacyapi.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.OrderService;
import org.openmrs.module.pharmacyapi.api.model.Prescription;
import org.openmrs.module.pharmacyapi.api.templates.ConceptTemplate;
import org.openmrs.module.pharmacyapi.api.templates.OrderProcessor;
import org.openmrs.module.pharmacyapi.api.templates.OrderTemplate;
import org.openmrs.module.pharmacyapi.api.templates.PatientTemplate;
import org.openmrs.module.pharmacyapi.api.util.BaseTest;
import org.openmrs.module.pharmacyapi.api.util.EntityFactory;

/**
 * @author Stélio Moiane
 */
public class PrescriptionServiceTest extends BaseTest {
	
	private PrescriptionService prescriptionService;
	
	@Before
	public void setUp() {
		this.prescriptionService = new PrescriptionServiceImpl();
	}
	
	@Test
	public void shouldFindPreparedPrescriptionsByPatient() {
		
		final Patient patient = EntityFactory.gimme(Patient.class, PatientTemplate.VALID);
		final List<DrugOrder> orders = EntityFactory.gimme(DrugOrder.class, 5, OrderTemplate.DRUG_ORDER);
		
		final OrderService orderService = mock(OrderService.class);
		when(orderService.getActiveOrders(patient, null, null, null)).thenReturn(new ArrayList<Order>(orders));
		this.prescriptionService.setOrderService(orderService);
		
		final ConceptService conceptService = mock(ConceptService.class);
		when(conceptService.getConceptByUuid("9d7408af-10e8-11e5-9009-0242ac110012")).thenReturn(
		    EntityFactory.gimme(Concept.class, ConceptTemplate.BEFORE_MEALS));
		this.prescriptionService.setConceptService(conceptService);
		
		final List<Prescription> prescriptions = this.prescriptionService.findPrescriptionsByPatient(patient);
		
		assertFalse(prescriptions.isEmpty());
		assertEquals(5, prescriptions.size());
		
		for (final Prescription prescription : prescriptions) {
			assertEquals("Antes das refeições", prescription.getDosingInstructions());
		}
	}
	
	@Test
	public void shouldCalculateDrugPickedUpAmountByOrder() {
		
		final DrugOrder order = EntityFactory.gimme(DrugOrder.class, OrderTemplate.REVISED, new OrderProcessor());
		
		final Double amount = this.prescriptionService.calculateDrugPikckedUp(order);
		
		assertEquals(20.0, amount, 0);
	}
}
