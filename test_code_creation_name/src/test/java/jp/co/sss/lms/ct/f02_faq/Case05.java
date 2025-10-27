package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
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
		//よくある質問リンクをクリック
		Object windowHandles[] = webDriver.getWindowHandles().toArray();
		int beforeTabCount = windowHandles.length;
		//よくある質問リンクをクリック

		WebElement element = webDriver.findElement(By.xpath("//a[text()=\"よくある質問\"]"));
		element.click();
		//新しいタブができるまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
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
		WebElement element = webDriver
				.findElement(By.xpath("//legend[contains(text(),\'キーワード検索\')]/div"));
		element.sendKeys("研修", Keys.ENTER);

		Thread.sleep(5000);

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// TODO ここに追加
	}

}
