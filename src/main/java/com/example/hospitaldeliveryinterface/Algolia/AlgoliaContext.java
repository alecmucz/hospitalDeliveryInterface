package com.example.hospitaldeliveryinterface.Algolia;
import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;
public class AlgoliaContext {

    public SearchClient algoliaClient() {
        SearchClient client = DefaultSearchClient.create("X25U8AMTMK","43f51f6de9b71f51db36921f37a0d78a");
        System.out.println("Algolia connection established");
        return client;
    }

}
