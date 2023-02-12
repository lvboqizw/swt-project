package computech.assignment;

import javax.persistence.*;

/**
 * abstract Assignment
 * @author Jannek Steinke
 */
@Entity
public abstract class Assignment {

    private @Id @GeneratedValue Long id;
    private AssignmentState state;
    private AssignmentType type;

    public Assignment(){}
    public Assignment(AssignmentState state, AssignmentType type){
        this.state = state;
        this.type = type;
    }
    /**
     *
     * @return AssignmentState state
     */
    public AssignmentState getState(){
        return state;
    }
    /**
     * 
     * @return Long id
     */
    public Long getId(){
        return id;
    }

    /**
     * 
     * @return AssignmentType type
     */
    public AssignmentType getType(){
        return type;
    }
    /**
     * sets the Assignment.state to Confirmed
     */
    public void setStateConfirmed(){
        state = AssignmentState.CONFIRMED;
    }
    /**
     * sets the Assignment.state to Deleted
     */
    public void setStateDeleted(){
        state = AssignmentState.DELETED;
    }
}
