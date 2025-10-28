package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		//リンクでログイン画面にアクセスし、タイトル名を確認
		goTo("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", (String) webDriver.getTitle());
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//ログインIDの入力
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		loginId.sendKeys("StudentAA01");
		//パスワードの入力
		WebElement password = webDriver.findElement(By.id("password"));
		password.sendKeys("StudentAA011", Keys.ENTER);
		//ページが表示されるまで待機
		visibilityTimeout(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[3]/button/i"), 5);
		//ログアウトボタンの有無でページの遷移を確認
		assertEquals("ログアウト", webDriver.findElement(By.className("glyphicon-log-out")).getText());
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//機能ボタンの押下
		WebElement functionButton = webDriver.findElement(By.xpath("//*[contains(text(),\"機能\")]"));
		functionButton.click();
		//プルダウン下のヘルプボタン押下
		WebElement helpButton = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]/ul/li[4]/a"));
		helpButton.click();
		//ヘルプ画面表示まで待機
		visibilityTimeout(By.xpath("//h2[contains(text(),\'ヘルプ\')]"), 5);
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws InterruptedException {
		//最初のタブ数を取得		
		Object beforewindowHandles[] = webDriver.getWindowHandles().toArray();
		int beforeTabCount = beforewindowHandles.length;

		visibilityTimeout(By.xpath("//a[text()=\"よくある質問\"]"), 5);
		//よくある質問リンクをクリック
		WebElement element = webDriver.findElement(By.xpath("//a[text()=\"よくある質問\"]"));
		element.click();
		Object windowHandles[] = webDriver.getWindowHandles().toArray();

		//新しいタブができるまで待機
		WebDriverWait wait = createWait(5);
		wait.until(driver -> driver.getWindowHandles().size() > beforeTabCount);

		//新しいページへ移動

		webDriver.switchTo().window((String) windowHandles[1]);
		visibilityTimeout(By.xpath("//h2[contains(text(),\'よくある質問\')]"), 5);

		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() throws Exception {
		//検索ワードの宣言
		String searchWord = "研修";
		//	キーワード入力欄を取得し、入力
		WebElement inputField = webDriver.findElement(By.xpath("//input[@type=\'text\']"));
		inputField.sendKeys(searchWord, Keys.ENTER);
		//検索ボタンを押下
		WebElement searchButton = webDriver
				.findElement(By.xpath("//input[@type=\'submit\'and contains(@value,\'検索\')]"));
		searchButton.click();
		//検索後のjavascliptの動作が終わるのを待機
		WebDriverWait wait = createWait(05);
		wait.until(
				webDriver -> ((JavascriptExecutor) webDriver)
						.executeScript("return document.readyState")
						.equals("complete"));

		//検索後に表示された複数の検索項目をリストで取得
		List<WebElement> articleElements = webDriver.findElements(By.xpath("//tbody/tr/td/dl/dt"));

		//検索項目毎に、質問または回答内に検索ワードが含まれているかを確認
		//
		//質問又は回答に検索ワードが含まれているかのbooleanを格納するリスト。要素が重複しないのでサイズが1であれば良い
		Set<Boolean> articleContainWord = new HashSet<>();
		articleContainWord.add(true);
		//リストで取得した要素を要素ごとに処理
		articleElements.forEach(element -> {
			//指定の要素までスクロールを行う
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", element);
			//質問をクリック
			element.click();
			//質問のテキスト取得
			String articleHeading = element.findElement(By.xpath("./span[2]")).getText();
			//回答のテキスト取得
			String articleText = element.findElement(By.xpath("../dd/span[2]")).getText();
			//質問又は回答に検索ワードが含まれているかの確認,
			//条件を満たしていればtrueを格納
			articleContainWord.add(articleHeading.contains(searchWord) || articleText.contains(searchWord));

		});
		//確認用のsetの要素がtrueのみであることを確認
		assertEquals(1, articleContainWord.size());
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() throws InterruptedException {
		//クリアボタンの取得
		WebElement clearButton = webDriver.findElement(By.xpath("//*[contains(@value,\'クリア\')]"));
		//クリアボタンの位置までスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", clearButton);
		clearButton.click();
		//検索キーワード入力欄が見える位置までスクロール
		scrollBy("-100");
		//検索キーワード入力欄が空欄であることを確認
		assertEquals("", webDriver.findElement(By.xpath("//input[@type=\'text\']")).getAttribute("value"));
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

}
