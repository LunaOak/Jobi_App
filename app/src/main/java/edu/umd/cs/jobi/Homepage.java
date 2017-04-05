package edu.umd.cs.jobi;

/* Main/Home Activity for Jobi App *********************************/
public class Homepage {

    private ArrayList<Position> postions;
    private ArrayList<Position> favorite_postions;
    private ArrayList<Company> companies;
    private ArrayList<Event> events;
    private Status status = Status.TODO;
    private Date date;

    public Homepage() {

    }

    /* Display ***************************************************/
    public void displayDate() {
      /* Display current date on App */
    }

    public void displayComingUp() {
      /* Create a list from events, we have to think of a way to sort them */
      /* Display the next # of events coming up */
    }

    public void displayStatus() {
      /* Display the person's current status */
    }

    public void displayManage() {
      /* Have four buttons in thus menu */
      /* Button for Company Activity */
      /* Button for Positions Activity */
      /* Button for Favorite Positions (Maybe we can just move this
         to the Position activity and display it as a tab) */
      /* Button for creating a new position */
    }

    /* Positions Handling ****************************************/
    public String getPositions() {
        return this.positions;
    }

    public void setPositions(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }


}