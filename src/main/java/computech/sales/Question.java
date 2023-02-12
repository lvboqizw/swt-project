package computech.sales;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;


/**
 * Class to store message containing subject and answer
 */
@Entity
public class Question {

	@javax.persistence.Id
	@Id @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;

	public String subject, content;


	private String answer;

	public Question(){}
	public Question(String subject, String content){
		this.subject = subject;
		this.content = content;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}

	public String getContent() {
		return content;
	}
	public String getSubject() {
		return subject;
	}
	public String toString() {
		return "SUBJECT: " + subject + ";   \n" + "CONTENT: " + content;
	}
}
