package com.example.hospitaldeliveryinterface.firebase;

import com.example.hospitaldeliveryinterface.model.DeliveryRequisition;
import com.example.hospitaldeliveryinterface.PharmaTracApp;
import com.example.hospitaldeliveryinterface.model.MitchTextTranslate;
import com.example.hospitaldeliveryinterface.model.NotifyMessg;
import com.example.hospitaldeliveryinterface.model.QueueSaves;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import net.suuft.libretranslate.Translator;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class DataBaseMgmt {

    //-------------------NEW METHODS CREATED FOR NOTIFYMESSG CLASS---------------->

    public static void addNotifyMessgToDB(String mssgDate, String mssgTime, String mssg){
        DocumentReference docRef = PharmaTracApp.fstore.collection("notifyHistory").document("notifyMessage");

        Map<String, Object> data = new HashMap<>();
        data.put("date", mssgDate);
        data.put("time", mssgTime);
        data.put("message", mssg);

        ApiFuture<WriteResult> result = docRef.set(data);
    }

    //----------------------------------------------------------------------------->

    /**************For initial firebase check for language stored in collection and its values**************/
    public static HashMap<String,String[]> initialLanguageCheck(TreeMap<String,String> initialLanguageMap){
        CollectionReference LanguageCollection = PharmaTracApp.fstore.collection("LanguageStorage");
        HashMap<String,String[]> storedLang = new HashMap<>();

        for (Map.Entry<String, String> entry : initialLanguageMap.entrySet()) {

            DocumentReference docRef = LanguageCollection.document(entry.getKey());
            ApiFuture<DocumentSnapshot> future = docRef.get();

            try {
                DocumentSnapshot document = future.get();

                if (document.exists()) {
                    System.out.println("IT EXISTS in LanguageStorage Collection: " + entry.getKey());

                    List<String> defaultSetUpValue = (List<String>) document.get("defualtSetUp");

                    String[] defaultSetUpArray = defaultSetUpValue.toArray(new String[0]);
                    storedLang.put(entry.getKey(), defaultSetUpArray);

                } else {
                    System.out.println("Need to add Language Defaults to LanguageStorage Collection: " + entry.getKey());
                    String[] defaultEnglsihText = MitchTextTranslate.defaultEnglishText();
                    String[] tempStirngArr = defaultEnglsihText;

                    for(int i = 0; i < defaultEnglsihText.length; i++){
                        tempStirngArr[i] = Translator.translate("en", entry.getValue(), defaultEnglsihText[i]);
                    }

                    storedLang.put(entry.getKey(), tempStirngArr);

                    List<String> StringListToDB = new ArrayList<>(Arrays.asList(tempStirngArr));

                    Map<String, Object> data = new HashMap<>();
                    data.put("defualtSetUp", StringListToDB);
                    ApiFuture<WriteResult> result = docRef.set(data);
                    try {
                        WriteResult writeResult = result.get();
                        System.out.println("Document created at: " + writeResult.getUpdateTime() + " for " + entry.getKey());
                    } catch (Exception e) {
                        System.err.println("Error adding document: " + e.getMessage());
                    }

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        }

        return storedLang;

    }

    /*******************************************************************************************/
    /**
     * adds a new deliveryRequisition to the pending collection of DB
     */
    public static void addToDB(DeliveryRequisition deliveryRequisition, String collectionName) {

        DocumentReference docRef = PharmaTracApp.fstore.collection(collectionName).document(deliveryRequisition.getOrderNumberDisplay());

        Map<String, Object> data = new HashMap<>();
        data.put("patientName", deliveryRequisition.getPatientName());
        data.put("location", deliveryRequisition.getPatientLocation());
        data.put("medication", deliveryRequisition.getMedication());
        data.put("dose", deliveryRequisition.getDose());
        data.put("numDoses", deliveryRequisition.getNumDoses());
        data.put("timeCreated", deliveryRequisition.getDateTime());
        data.put("notes", deliveryRequisition.getNotes());
        data.put("deliveredBy", deliveryRequisition.getDeliveredBy() != null ? deliveryRequisition.getDeliveredBy() : "");
        data.put("createdBy", deliveryRequisition.getCreatedBy() != null ? deliveryRequisition.getCreatedBy() : "");
        data.put("updatedBy", deliveryRequisition.getUpdatedBy() != null ? deliveryRequisition.getUpdatedBy() : "");
        if (collectionName.equals("pendingDeliveries")) {
            data.put("status", "p");
        } else {
            data.put("status", "c");
        }
        //add who entered the order
        ApiFuture<WriteResult> result = docRef.set(data);


    }

    /**
     * edits an existing order
     *
     * @param collectionName name of collection the order is currently in
     * @param orderNumber    order number of order you want to edit
     */
    public static void editOrder(String collectionName, String orderNumber, DeliveryRequisition order) {

        Map<String, Object> data = new HashMap<>();
        data.put("patientName", order.getPatientName());
        data.put("location", order.getPatientLocation());
        data.put("medication", order.getMedication());
        data.put("dose", order.getDose());
        data.put("numDoses", order.getNumDoses());
        data.put("timeCreated", order.getDateTime());
        data.put("notes", order.getNotes());
        data.put("updatedBy", Optional.ofNullable(order.getUpdatedBy()).orElse(""));

        ApiFuture<WriteResult> future = PharmaTracApp.fstore.collection(collectionName).document(orderNumber).set(data);
        //add who edited and what time they edited
        //DocumentReference docRef = PharmaTracApp.fstore.collection(collectionName).document(orderNumber);
        //ApiFuture<WriteResult> writeResult = docRef.update("timeCreated", FieldValue.serverTimestamp());

    }



    /**
     * @return the total number of orders ever created from the DB
     */
    public static int getTotalNumOrders() {
        CollectionReference statistics = PharmaTracApp.fstore.collection("statistics");

        DocumentReference statisticsRef = statistics.document("numOrders");
        ApiFuture<DocumentSnapshot> future = statisticsRef.get();
        try {
            DocumentSnapshot document = future.get();
            Number numTotalOrders = (Number) document.get("totalNumOrders");
            return numTotalOrders.intValue();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * gives you reference to query a collection
     *
     * @param collectionName name of collection  you want to query
     * @return query the collection you want to query
     */
    public static ApiFuture<QuerySnapshot> getCollection(String collectionName) {
        ApiFuture<QuerySnapshot> query = PharmaTracApp.fstore.collection(collectionName).get();

        return query;
    }

    /**
     * Reads all order from a collection, pending or completed and puts then into a linked list
     *
     * @param collectionName collection you want to build the queue from
     * @return queue of all orders in the target collection
     */
    public static Queue<DeliveryRequisition> buildQueue(QuerySnapshot querySnaps, String collectionName) {

        Queue<DeliveryRequisition> requisitionQueue = new LinkedList<>();

        List<QueryDocumentSnapshot> documents = querySnaps.getDocuments();

        for(QueryDocumentSnapshot document : documents) {

            DeliveryRequisition order = new DeliveryRequisition(
                    document.getId()
                    ,document.getString("timeCreated")
                    ,document.getString("patientName")
                    , document.getString("location")
                    , document.getString("medication")
                    , document.getString("dose")
                    , document.getString("numDoses")
                    , document.getString("notes"),
                    document.getString("deliveredBy"),
                    document.getString("createdBy"),
                    document.getString("updatedBy")
            );

            requisitionQueue.add(order);
                //System.out.println("Here are the build Queue Results: " + order.toString());
        }

        if(collectionName.equals("completedDeliveries")){
            QueueSaves.setCompletedLatest(requisitionQueue);
        }

        if(collectionName.equals("pendingDeliveries")){
            QueueSaves.setPendingLatest(requisitionQueue);
        }

        return requisitionQueue;

    }

    /**
     * moves DeliveryRequisition from pending to completed
     * or vice versa
     *
     * @param orderNumber    DeliveryRequisition to be swapped
     * @param collectionFrom origin collection
     * @param collectionTo   destination collection
     */
    public static void swapDB(String orderNumber, String collectionFrom, String collectionTo) {
        System.out.println("SwapDB called");
        /*
        copy the data from it into delivery rec
        delete the data from pending
        add the new data to completed
         */
        DeliveryRequisition order = findOrder(orderNumber, collectionFrom);
        if (order != null) {
            deleteFromDB(orderNumber, collectionFrom);
            addToDB(order, collectionTo);
        }

    }

    /**
     * deletes an order form the specified collection of the database
     *
     * @param orderNumber    order you want to delete
     * @param collectionName collection you are deleting the target order from
     */
    public static void deleteFromDB(String orderNumber, String collectionName) {
        System.out.println("deleteFromDB called");
        PharmaTracApp.fstore.collection(collectionName).document(orderNumber).delete();
    }


    /**
     * finds and returns an order from a specific collection of the database
     *
     * @param orderNumber    order you want to delete
     * @param collectionName collection you are deleting the target order from
     * @return order and its data
     */
    public static DeliveryRequisition findOrder(String orderNumber, String collectionName) {
        CollectionReference statistics = PharmaTracApp.fstore.collection(collectionName);

        DocumentReference statisticsRef = statistics.document(orderNumber);
        ApiFuture<DocumentSnapshot> future = statisticsRef.get();
        try {
            DocumentSnapshot document = future.get();
            DeliveryRequisition order = new DeliveryRequisition(
                    document.getId(),
                    document.getString("timeCreated"),
                    document.getString("patientName"),
                    document.getString("location"),
                    document.getString("medication"),
                    document.getString("dose"),
                    document.getString("numDoses"),
                    document.getString("notes"),
                    document.getString("deliveredBy"),
                    document.getString("createdBy"),
                    document.getString("updatedBy")
            );
            return order;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public static Queue<DeliveryRequisition> search(String searchTerm, String collectionName, String searchParameter) {
        if (collectionName.equals("Pending")) {
            collectionName = "pendingDeliveries";
        }
        if (collectionName.equals("Completed")) {
            collectionName = "completedDeliveries";
        }

        Queue<DeliveryRequisition> searchResults = new LinkedList<>();
        CollectionReference collectionReference = PharmaTracApp.fstore.collection(collectionName);

        Query query = collectionReference.whereGreaterThanOrEqualTo(searchParameter, searchTerm)
                .whereLessThanOrEqualTo(searchParameter, searchTerm + "\uf8ff");

        try {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                DeliveryRequisition order = new DeliveryRequisition(
                        document.getId()
                        , document.getString("timeCreated")
                        , document.getString("patientName")
                        , document.getString("location")
                        , document.getString("medication")
                        , document.getString("dose")
                        , document.getString("numDoses")
                        , document.getString("notes"),
                        document.getString("deliveredBy"),
                        document.getString("createdBy"),
                        document.getString("updatedBy")
                );

                searchResults.add(order);

            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error executing search");
        }
        return searchResults;
    }

    //create document for employee collection
    public static void addCreateUserDB(String employeeID, String firstName, String lastName, String password, String username, String email) {
        DocumentReference docRef = PharmaTracApp.fstore.collection("employees").document(employeeID);

        Map<String, Object> data = new HashMap<>();
        data.put("First", firstName);
        data.put("Last", lastName);
        data.put("Password", password);
        data.put("Username", username);
        data.put("email", email);
        data.put("loginStatus", "false");

        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public static Map<String, Object> retrieveUserData(String employeeID,String collectionName) throws InterruptedException, ExecutionException {
        if (employeeID == null || employeeID.isEmpty()) {
            System.out.println("EmployeeID is not provided.");
            return null; // Or handle the scenario appropriately
        }

        // Reference to the document for the specified employeeID in the "employees" collection
        CollectionReference collection = PharmaTracApp.fstore.collection(collectionName);
        DocumentReference docRef = collection.document(employeeID);

        // Retrieve the document snapshot asynchronously
        ApiFuture<DocumentSnapshot> future = docRef.get();


        try {
            // Wait for the asynchronous task to complete
            DocumentSnapshot document = future.get();

            // Check if the document exists
            if (document.exists()) {
                // Extract data from the document
                return document.getData();
            } else {
                // Document not found
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle exceptions
            System.out.println("Error executing search: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    public static void updateLoginStatus(String employeeId, String status) {

        // Update an existing document
        DocumentReference docRef = PharmaTracApp.fstore.collection("employees").document(employeeId);

        // (async) Update one field
        ApiFuture<WriteResult> future = docRef.update("loginStatus", status);
    }

    public static Queue<DeliveryRequisition> getQueue(String collectionName) {
        Queue<DeliveryRequisition> queue = new LinkedList<>();
        ApiFuture<QuerySnapshot> future = PharmaTracApp.fstore.collection(collectionName).get();
        try {
            QuerySnapshot querySnapshot = future.get();  // This blocks on the future, consider using an asynchronous approach if performance is an issue.
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                DeliveryRequisition order = new DeliveryRequisition(
                        document.getId(),
                        document.getString("timeCreated"),
                        document.getString("patientName"),
                        document.getString("location"),
                        document.getString("medication"),
                        document.getString("dose"),
                        document.getString("numDoses"),
                        document.getString("notes"),
                        document.getString("deliveredBy"),
                        document.getString("createdBy"),
                        document.getString("updatedBy")
                );
                queue.add(order);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("Error retrieving documents: " + e.getMessage());
        }
        return queue;
    }

}










