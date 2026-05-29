package com.example.myapplication

import java.util.Random

enum class Mood {
    GREETING,
    NO_TRANSACTIONS_TODAY,
    OVERSPENDING,
    SAVING,
    NORMAL,
    OVER_LIMIT
}

data class Phrase(val text: String)

object CharacterPhrases {

    private val random = Random()

    val monkPhrases: Map<Mood, List<Phrase>> = mapOf(
        Mood.GREETING to listOf(
            Phrase("Доброе утро. Твой кошелёк — это отражение внутреннего покоя"),
            Phrase("Приветствую, путник. Готов ли ты к осознанным тратам?"),
            Phrase("Дыши глубже. Сегодня новый день для мудрых решений")
        ),
        Mood.NO_TRANSACTIONS_TODAY to listOf(
            Phrase("Тишина в финансах — тоже практика. Но не затягивай"),
            Phrase("Пустота в кошельке или в записях? Наполни и то, и другое"),
            Phrase("Ты не внёс ни одной записи. Медитация — это хорошо, но баланс — тоже")
        ),
        Mood.OVERSPENDING to listOf(
            Phrase("Ты потратил больше обычного. Спроси себя — было ли оно того?"),
            Phrase("Материальное не приносит счастья. Но перерасход приносит стресс. Отпусти лишнее"),
            Phrase("Твой бюджет вышел из равновесия. Восстановим гармонию?")
        ),
        Mood.OVER_LIMIT to listOf(
            Phrase("Ты превысил лимит. Это знак. Остановись и подумай"),
            Phrase("Лимит исчерпан. Время для осознанности, а не для трат"),
            Phrase("Твои траты вышли за пределы. Возвращайся к простоте")
        ),
        Mood.SAVING to listOf(
            Phrase("Твой внутренний храм наполняется. Продолжай в том же духе"),
            Phrase("Умеренность — путь к свободе. Ты на верном пути"),
            Phrase("Баланс восстановлен. Дыши глубже. Ты молодец")
        ),
        Mood.NORMAL to listOf(
            Phrase("Всё в гармонии. Деньги — это энергия, и она течёт плавно"),
            Phrase("Спокойствие и баланс. Продолжай в том же ритме"),
            Phrase("Твоё финансовое дзен сегодня в порядке")
        )
    )

    val temschikPhrases: Map<Mood, List<Phrase>> = mapOf(
        Mood.GREETING to listOf(
            Phrase("Ну чё, бабки считать будем или просто посидим?"),
            Phrase("Салют, мамкин инвестор! Чё там по кэшу?"),
            Phrase("Открывай приложение, ща будем тему мутить")
        ),
        Mood.NO_TRANSACTIONS_TODAY to listOf(
            Phrase("Эй, ты там уснул? Транзакции сами себя не запишут"),
            Phrase("Тишина в эфире. Ты чё, на мели или просто ленивый?"),
            Phrase("Ау! Где цифры? Тема не взлетит без отчётов")
        ),
        Mood.OVERSPENDING to listOf(
            Phrase("Ого, да ты в крипту походу ушёл. Минус на минусе, братан"),
            Phrase("Сколько?! Ты чё, ресторан купил? Сушняк бюджету"),
            Phrase("Так, тема с перерасходом — полный скам. Завязывай")
        ),
        Mood.OVER_LIMIT to listOf(
            Phrase("Лимит пробит! Ты чё, миллионер? Остынь"),
            Phrase("Всё, тема схлопнулась. Лимит — в ноль"),
            Phrase("По лимиту всё, братан. Дальше — за свой счёт")
        ),
        Mood.SAVING to listOf(
            Phrase("Опа, да ты в плюсе. Респект. Тема реально качает"),
            Phrase("Красава! Дисциплина — топ. Продолжай мутить"),
            Phrase("Ты сегодня сэкономил — красавчик. Тема огонь")
        ),
        Mood.NORMAL to listOf(
            Phrase("Всё ровно, движуха по плану. Держим тему"),
            Phrase("Бабки на месте, движ в порядке. Молоток"),
            Phrase("Нормуль. Тема катит, продолжаем")
        )
    )

    val caringPhrases: Map<Mood, List<Phrase>> = mapOf(
        Mood.GREETING to listOf(
            Phrase("Доброе утро, солнышко! Как там наши финансы поживают?"),
            Phrase("Привет, родной! Не забыл покушать? И бюджет проверь заодно"),
            Phrase("С добрым утром! Я тут волнуюсь — всё ли в порядке с деньгами?")
        ),
        Mood.NO_TRANSACTIONS_TODAY to listOf(
            Phrase("Ты сегодня ничего не записал... Я переживаю. Всё хорошо?"),
            Phrase("Опять забыл внести расходы? Давай вместе, я помогу"),
            Phrase("Пустой дневник трат... У тебя точно всё в порядке?")
        ),
        Mood.OVERSPENDING to listOf(
            Phrase("Ой, милый, ты потратил больше обычного. Надеюсь, на что-то нужное?"),
            Phrase("Перерасход... Я не ругаю, но давай поаккуратнее, хорошо?"),
            Phrase("Сердце болит, когда вижу такие траты. Пожалей меня, сбереги")
        ),
        Mood.OVER_LIMIT to listOf(
            Phrase("Родной, ты превысил лимит! Я волнуюсь за нас"),
            Phrase("Лимит закончился... Давай подождём до следующего месяца?"),
            Phrase("Милый, лимит исчерпан. Пожалуйста, остановись")
        ),
        Mood.SAVING to listOf(
            Phrase("Умница моя! Так горжусь твоей бережливостью!"),
            Phrase("Ты такой молодец! Сэкономил — как будто заработал"),
            Phrase("Спасибо, что заботишься о нашем бюджете!")
        ),
        Mood.NORMAL to listOf(
            Phrase("Всё хорошо, всё спокойно. Обнимаю тебя мысленно"),
            Phrase("Баланс в порядке, на душе тепло. Ты у меня самый лучший"),
            Phrase("День прошёл спокойно. Горжусь тобой")
        )
    )

    val trainerPhrases: Map<Mood, List<Phrase>> = mapOf(
        Mood.GREETING to listOf(
            Phrase("Подъём! Финансы не прокачаются сами. Проверь баланс. Погнали!"),
            Phrase("Утро — время дисциплины. Пока ленивые спят, мы считаем"),
            Phrase("Разгар дня. Не расслабляй булки. Покажи, что там по расходам")
        ),
        Mood.NO_TRANSACTIONS_TODAY to listOf(
            Phrase("Нулевой день — нулевой результат. Качай финансовую мышцу!"),
            Phrase("ТРИ ЧАСА НИ ОДНОЙ ЗАПИСИ! Отговорки не принимаются. Вноси!"),
            Phrase("Без записей ты слепой. А слепой атлет — травмированный атлет. Работай!")
        ),
        Mood.OVERSPENDING to listOf(
            Phrase("ПЕРЕБОР! Ты что, финансово переел? Срочно сушка бюджета!"),
            Phrase("Красная зона. Планка упала. Снимаем 10% нагрузки!"),
            Phrase("Жёстко. Это не провал, это обратная связь. Тренируемся дальше")
        ),
        Mood.OVER_LIMIT to listOf(
            Phrase("ЛИМИТ ПРОБИТ! Штрафной круг! Больше ни копейки!"),
            Phrase("Ты превысил лимит. Это disqualification. Исправляйся!"),
            Phrase("Лимит — это твоя планка. А ты её уронил. Поднимай!")
        ),
        Mood.SAVING to listOf(
            Phrase("Вот это форма! Бюджет в плюсе. Сухой, поджарый. Ты — зверь!"),
            Phrase("План перевыполнен. Месяц назад ты сдувался, а сейчас — дисциплина!"),
            Phrase("Ты сегодня сэкономил. Это прогресс. Продолжай в том же духе!")
        ),
        Mood.NORMAL to listOf(
            Phrase("Всё по плану. Дисциплина на уровне. Продолжаем тренировку"),
            Phrase("Баланс ровный. Скучновато, но надёжно. Может, рискнём инвестицией?"),
            Phrase("Норма. Не отдыхай слишком долго. Завтра новый подход")
        )
    )

    fun getPhrase(style: String, mood: Mood): String {
        val phrases = when (style) {
            "Монах" -> monkPhrases[mood]
            "Темщик" -> temschikPhrases[mood]
            "Заботливый" -> caringPhrases[mood]
            "Тренер" -> trainerPhrases[mood]
            else -> monkPhrases[mood]
        }
        return phrases?.get(random.nextInt(phrases.size))?.text ?: "Привет!"
    }

    fun getRandomPhrase(style: String, hasTransactionsToday: Boolean,
                        isOverSpending: Boolean, isOverLimit: Boolean,
                        isSaving: Boolean): String {
        val mood = when {
            isOverLimit -> Mood.OVER_LIMIT
            !hasTransactionsToday -> Mood.NO_TRANSACTIONS_TODAY
            isOverSpending -> Mood.OVERSPENDING
            isSaving -> Mood.SAVING
            else -> Mood.NORMAL
        }
        return getPhrase(style, mood)
    }
}