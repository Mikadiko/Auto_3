package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardOrderTest {

    @BeforeEach
    void openingWebsite(){
        open("http://localhost:9999");
    }


    @Test
    void positiveDebitCardApplication() {
        $(".heading").shouldHave(exactText("Заявка на дебетовую карту")); //проверяем текст
        $(".heading_size_s").shouldHave(exactText("Персональные данные"));
        SelenideElement form = $(".form");
        form.$("[data-test-id=name] input").setValue("Ольга");
        form.$("[data-test-id=name] .input__sub").shouldHave(exactText("Укажите точно как в паспорте"));
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=phone] .input__sub").shouldHave(exactText("На указанный номер моб. тел. будет " +
                "отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно."));
        form.$("[data-test-id=agreement]").click();
        form.$("[data-test-id=agreement] .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки " +
                "и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй"));
        $(".button_view_extra").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер " +
                "свяжется с вами в ближайшее время."));

    }

    @Test
    void positiveWithHyphen() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Анна-Мария");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер " +
                "свяжется с вами в ближайшее время."));
    }

    @Test
    void positiveOneLetter() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("А");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер " +
                "свяжется с вами в ближайшее время."));
    }

    @Test
    void positive60Letter() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("ОльгаОльгаОльгаОльгаОльгаОльгаОльгаОльгаОльгаОльгаОльгаОльга");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер " +
                "свяжется с вами в ближайшее время."));
    }

    @Test
    void negativeLatinAlphabet() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Olga");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные " +
                "неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void negativeInvalidCharacters() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("!%;№");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные " +
                "неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void negativeEmptyFieldName() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("79645004442");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для " +
                "заполнения"));
    }

    @Test
    void negativeIncorrectPhoneNumber() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Ольга");
        form.$("[data-test-id=phone] input").setValue("123456789");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. " +
                "Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void negativeEmptyFieldPhone() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Ольга");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для " +
                "заполнения"));
    }

    @Test
    void negativeCheckboxIsNotMarked() {
        SelenideElement form = $("form");
        form.$("[data-test-id=name] input").setValue("Ольга");
        form.$("[data-test-id=phone] input").setValue("+79645004442");
        //form.$("[data-test-id=agreement]").click();
        $(".button_view_extra").click();
        form.$("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями" +
                " обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных " +
                "историй"));
    }
}
