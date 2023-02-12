package computech.accountancy;


import lombok.Getter;

/**
 * save the information for search
 */
@Getter
public class SearchForm {

	private final String searchRange;

	private final String start;

	private final String end;

	private final String username;


	public SearchForm(String searchRange, String start, String end, String username) {
		this.searchRange = searchRange;
		this.start = start;
		this.end = end;
		this.username = username;
	}
	public String getStart(){
		return start;
	}
	public String getSearchRange(){
		return searchRange;
	}
	public String getEnd(){
		return end;
	}
	public String getUsername(){
		return username;
	}
}
