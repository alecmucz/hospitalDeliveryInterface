package com.example.hospitaldeliveryinterface.model;

import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToggleTrackingTest {

    private static class HomepageControllerStub extends HomepageController {
        boolean newDeliveryToggled = false;

        @Override
        public void toggleNewDelivery() {
            newDeliveryToggled = true;
        }

        public boolean isToggleNewDeliveryCalled() {
            return newDeliveryToggled;
        }

        public void resetToggleNewDeliveryCalled() {
            newDeliveryToggled = false;
        }
    }

    private HomepageControllerStub homepageControllerStub;

    @BeforeEach
    void setUp() {
        homepageControllerStub = new HomepageControllerStub();
        ToggleTracking.setHomepageController(homepageControllerStub);
        ToggleTracking.clearOrders();
    }

    @Test
    void testSelectNode() {
        HBox node = new HBox();
        DeliveryRequisition requisition = new DeliveryRequisition();
        requisition.setOrderNumberDisplay("1234");
        node.setUserData(requisition);
        ToggleTracking.selectNode(node);
        assertTrue(ToggleTracking.getSelectedOrders().contains(requisition), "Requisition should be added to selected orders.");
        assertEquals("1234", ToggleTracking.getSelectedCardOrderNum(), "Selected card order number should be set.");
        assertTrue(homepageControllerStub.isToggleNewDeliveryCalled(), "toggleNewDelivery should be called upon selecting a node.");
    }
    @Test
    void testDeselectNode() {
        HBox node = new HBox();
        DeliveryRequisition requisition = new DeliveryRequisition();
        requisition.setOrderNumberDisplay("1234");
        node.setUserData(requisition);

        ToggleTracking.selectNode(node);
        assertTrue(ToggleTracking.getSelectedOrders().contains(requisition), "Requisition should be added to selected orders upon selection.");
        assertEquals("1234", ToggleTracking.getSelectedCardOrderNum(), "Selected card order number should be set upon selection.");

        ToggleTracking.deselectNode(node);
        assertFalse(ToggleTracking.getSelectedOrders().contains(requisition), "Requisition should be removed from selected orders upon deselection.");

        if (ToggleTracking.getSelectedOrders().isEmpty()) {
            assertNull(ToggleTracking.getSelectedCardOrderNum(), "Selected card order number should be null if no orders are selected.");
        } else {
            assertEquals(ToggleTracking.getSelectedOrders().iterator().next().getOrderNumberDisplay(), ToggleTracking.getSelectedCardOrderNum(), "Selected card order number should update to the next available order.");
        }
        assertTrue(homepageControllerStub.isToggleNewDeliveryCalled(), "toggleNewDelivery should be called upon deselecting a node.");
        homepageControllerStub.resetToggleNewDeliveryCalled();
    }
    @Test
    void testClearOrders() {
        ToggleTracking.clearOrders();
        assertTrue(ToggleTracking.getSelectedOrders().isEmpty(), "Selected orders should be empty after clearing.");
        assertNull(ToggleTracking.getSelectedCardOrderNum(), "Selected card order number should be null after clearing.");
        assertTrue(homepageControllerStub.isToggleNewDeliveryCalled(), "toggleNewDelivery should be called upon clearing orders.");
    }
}