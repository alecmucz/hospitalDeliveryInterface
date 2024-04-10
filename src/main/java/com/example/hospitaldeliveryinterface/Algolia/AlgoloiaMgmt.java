package com.example.hospitaldeliveryinterface.Algolia;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;
import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;

import java.time.LocalDateTime;

public class AlgoloiaMgmt {
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

}
