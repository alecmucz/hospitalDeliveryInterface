package com.example.hospitaldeliveryinterface.Algolia;

import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;
import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AlgoliaMgmt {
    private static SearchIndex<DeliveryRequisition> index = PharmaTracApp.aClient.initIndex("orders", DeliveryRequisition.class);

    public static void createNewIndex() {
        DeliveryRequisition test = new DeliveryRequisition("1234567",
                "test",
                "Jon Doe",
                "18N",
                "test",
                "1000mg",
                "test",
                "test",
                "test",
                "test");

        index.saveObject(test).waitTask();
    }
    public static void search(String searchTerm) {
        Queue<DeliveryRequisition> searchResultsQueue = new LinkedList<>();

        SearchIndex index = PharmaTracApp.aClient.initIndex("orders", DeliveryRequisition.class);

        SearchResult<DeliveryRequisition> searchResultsAlgolia = index.search(new Query(searchTerm));

        List<DeliveryRequisition> searchResultsList = searchResultsAlgolia.getHits();
        searchResultsQueue.addAll(searchResultsList);
        System.out.println(searchResultsQueue.poll());

        //List<SearchHit<DeliveryRequisition>> hits = results.getHits();

       //for(SearchHit<DeliveryRequisition> searchHit : results.getHits()) {
         //    searchResults.offer(searchHit.getContent());
     //   }
    }
}
