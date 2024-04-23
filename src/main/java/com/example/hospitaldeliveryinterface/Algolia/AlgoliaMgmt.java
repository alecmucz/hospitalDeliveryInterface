package com.example.hospitaldeliveryinterface.Algolia;

import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;
import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AlgoliaMgmt {
    private static SearchIndex<DeliveryRequisition> index = PharmaTracApp.aClient.initIndex("orders", DeliveryRequisition.class);

    /**
     * adds a delivery requisition to the Algolia DB, if a record already exist with the same ID the record will be overwritten
     * @param deliveryRequisition, requisition to bea dded/edited in Algolia
     */
    public static void createNewIndex(DeliveryRequisition deliveryRequisition) {
        index.saveObject(deliveryRequisition).waitTask();
    }

    /**
     * takes a search term and queries the Algolia DB and returns a queue of delivery requisitions that match teh search term
     * @param searchTerm, user input for what to search for
     */
    public static Queue<DeliveryRequisition> searchAlgolia(String searchTerm) {
        Queue<DeliveryRequisition> searchResultsQueue = new LinkedList<>();

        SearchIndex index = PharmaTracApp.aClient.initIndex("orders", DeliveryRequisition.class);

        SearchResult<DeliveryRequisition> searchResultsAlgolia = index.search(new Query(searchTerm));

        List<DeliveryRequisition> searchResultsList = searchResultsAlgolia.getHits();
        searchResultsQueue.addAll(searchResultsList);

        return searchResultsQueue;
    }

}
