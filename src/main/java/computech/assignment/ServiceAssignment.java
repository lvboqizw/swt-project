package computech.assignment;

import javax.persistence.*;

import computech.sales.Question;

/**
 * Assignment for Normal Service
 * @author Jannek Steinke
 */
@Entity
public class ServiceAssignment extends Assignment{

    @OneToOne(cascade = {CascadeType.ALL})
    private Question question;
    private Long userid;

    public ServiceAssignment(){}
    /**
     * 
     * @param question
     * @param userid
     */
    public ServiceAssignment(Question question, Long userid){
        super(AssignmentState.OPEN, AssignmentType.SERVICE);
        this.question = question;
        this.userid = userid;
    }
    /**
     * 
     * @return Question
     */
    public Question getQuestion(){
    	return question;
	}

    /**
     * 
     * @return Long
     */
	public Long getUserid(){
    	return userid;
	}








}