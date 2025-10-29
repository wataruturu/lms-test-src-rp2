package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		WebElement element = webDriver
				.findElement(By.xpath("//div[@class=\'container\']/div[3]//tr[1]//input[@value=\'詳細\']"));
		element.click();
		visibilityTimeout(By.xpath("//h2[contains(text(),\'Java概要\')]"), 5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement submitElement = webDriver.findElement(By.xpath("//*[@type=\'submit\']"));
		submitElement.click();
		visibilityTimeout(By.xpath("//h2[contains(text(),\'日報【デモ】\')]"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		//報告内容となる文章
		String text = "報告内容";
		//報告入力欄に文章を入力
		WebElement insertField = webDriver.findElement(By.xpath("//textarea"));
		insertField.clear();
		insertField.sendKeys(text, Keys.ENTER);
		//提出ボタンを押下
		WebElement submitButton = webDriver.findElement(By.xpath("//*[contains(text(),\'提出する\')]"));
		submitButton.click();
		//確認ボタンが変更されていることを確認(「確認する」が含まれていること)
		By confirmButton = By.xpath("//*[@type=\'submit\']");
		visibilityTimeout(confirmButton, 5);
		assertTrue(webDriver.findElement(confirmButton).getAttribute("value").contains("確認する"));

		//エビデンス取得
		getEvidence(new Object() {
		});

	}

}
