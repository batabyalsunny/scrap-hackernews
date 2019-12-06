/**
 * 
 */
package ml.bootcode;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import ml.bootcode.core.scrapers.ArticleListScraper;
import ml.bootcode.entities.Article;

/**
 * @author sunnyb
 *
 */
public class ScrapHackernewsApp {

	// Url to scrap.
	private final static String URL = "https://news.ycombinator.com/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Scrap and get the data.
		try {
			ArticleListScraper scraper = new ArticleListScraper(URL);

			// Get the data.
			List<Article> articles = scraper.scrap();

			ScrapHackernewsApp app = new ScrapHackernewsApp();

			// Check the first argument flag.
			switch (args[0]) {
			case "-a":
				app.listAuthors(articles);
				break;

			case "-s":
				app.listArticles(articles);
				break;

			default:
				System.out.println("What to list out? Please chose an option: -a for authors -s for stories.");
				break;
			}

		} catch (IOException | ParseException e) {
			System.out.println("Unable to reach " + URL);
			e.printStackTrace();
		}
	}

	/**
	 * Prints out user name and his Karma points from top page of.
	 */
	public void listAuthors(List<Article> articles) {

		System.out.println("All authors and their karma points");
		System.out.println("====================================\n");

		// Print
		Iterator<Article> it = articles.iterator();
		while (it.hasNext()) {
			Article article = it.next();
			System.out.println("Author: " + article.getAuthor().getUserName());
			System.out.println("Karma Point: " + article.getAuthor().getKarma() + "\n");
		}
	}

	/**
	 * Lists all stories sorted by comment count.
	 */
	public void listArticles(List<Article> articles) {

		// Sort the articles by comment count.
		Collections.sort(articles, new Comparator<Article>() {

			@Override
			public int compare(Article o1, Article o2) {
				return o1.getCommentCount().compareTo(o2.getCommentCount());
			}
		});

		System.out.println("All stories sorted by number of comments");
		System.out.println("==========================================\n");

		// Print.
		Iterator<Article> it = articles.iterator();
		while (it.hasNext()) {
			Article article = it.next();
			System.out.println("Title: " + article.getTitle());
			System.out.println("Comment Count: " + article.getCommentCount() + "\n");
		}
	}
}
