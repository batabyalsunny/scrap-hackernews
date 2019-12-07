/**
 * 
 */
package ml.bootcode.entities;

/**
 * Author.
 * 
 * @author sunnyb
 *
 */
public class Author {

	private String userName;
	private Integer karma;
	private String about;

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the karma
	 */
	public Integer getKarma() {
		return karma;
	}

	/**
	 * @param karma the karma to set
	 */
	public void setKarma(Integer karma) {
		this.karma = karma;
	}

	/**
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * @param about the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}
}
