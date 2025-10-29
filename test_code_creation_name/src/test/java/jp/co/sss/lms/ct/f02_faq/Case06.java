package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	void test04() {
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
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		//カテゴリ検索の対象ワード
		String targetWord = "【研修関係】";

		WebElement element = webDriver.findElement(By.xpath("//a[contains(text(),\'" + targetWord + "\')]"));
		element.click();

		//表示された検索結果項目をリストで取得
		List<WebElement> articleElements = webDriver.findElements(By.xpath("//tbody/tr/td/dl/dt"));
		//検索項目の最後の項目が画面内に入るようスクロール
		WebElement lastElement = articleElements.get(articleElements.size() - 1);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", lastElement);

		//検索の後にURLが変更されていることを確認
		String targetUrlString = "frequentlyAskedQuestionCategoryId=1";
		assertTrue(webDriver.getCurrentUrl().contains(targetUrlString));
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		//表示された検索結果項目をリストで取得
		List<WebElement> articleElements = webDriver.findElements(By.xpath("//tbody/tr/td/dl/dt"));
		//検索項目の確認用リスト
		List<Boolean> articleContainWord = new ArrayList<>();

		articleElements.forEach(element -> {
			//指定の要素までスクロールを行う
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", element);
			//質問をクリック
			element.click();
			//クリック後に回答が出てきたか確認。文章が出てきたらfalseがリストに格納される
			articleContainWord.add(element.findElement(By.xpath("../dd/span[2]")).getText().isEmpty());

		});
		assertFalse(articleContainWord.contains(true));
		//エビデンス取得
		getEvidence(new Object() {
		});
	}

}
