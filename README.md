3/6/2024 - Push by Alec, Pending stage implementation

4/9/24 10:49 - Will Edit
- Changed ordercard UI to better display the notes secition 
- added fuction to track all user actions taken on an order
- tracks creation, edits and moves between queues
- added reports page for new seach function
- supports swapping between pages

3/9/2024 3:04 am - Mitchell Edit:
- Revise Homepage UI
- Created New Delivery Form
- Orders Display on HomePage
- Error handling with invalid input on New Delivery Form
- Pending Classes added for Queue and order storage
- Completed Class setup but not implemented yet
- orderCard created for cloning orderUI to homepage
- orderCardUI Controller for updating data from Queue linkedlist
- DateSetUpFixed on Delivery Requisitions
- Method to generate Order number, I decide the ordernumbers are based  on todays date then "00" and then orderNumberCounter
- ScrollPane added to Homepage for orderQueue list
- To test make sure you are on my branch "Mitchell_branch" / Didnt merge to main/master branch yet.

3/9/2024 2:21pm -Mitchell Edit:
-Able to toggle between Pending and Completed Tab

3/9/2024 6:09 pm -Mitchell Edit:
-Adjust top navbar
-setting side nav bar implemented

3/11/2024 1:00 pm - Alec Edit:
- Toolbar changes according to page
- New Delivery Popup window (note: Edit Delivery currently has the same popup to Deliver Windows )

3/14/2024 12:38 am - Mitchell Edit:
- Redesign application UI
- reimplemented new delivery to the homepage
- The edit and delivered button works
- Completed and Pending tabs display orders

3/17/2024 3:01 AM - Mitchell/William Edit:
- created a new method for sending and retrieving documents from Google Firebase
- Pending and Completed class has been removed
- files have been organized to the specified package
- Display Pending and Completed Queue updates in real-time for all applications

3/17/2024 11:16 AM - Alec Edit:
- Bug Fix: If the user enables the LoginVBOX pane with the login button and closes the settings tab, the widgets within LoginVBOX would still be left over in the New Delivery Pane

3/27/24 11:08 PM - Alec Edit:
 - Cleaned up GUI (set proper widths throughout toolbar and navbars so buttons arent being cut off)
 - Made search button nice (should probably implement search drop down to function the same as the settings button and navbar)
 - Will be working on selecting multiple orders and performing functions (Deliver,Send back to Pending,Delete) those selections.
