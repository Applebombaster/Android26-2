@file:OptIn(ExperimentalMaterial3Api::class)

package ru.urfu.droidpractice1.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.SecondActivity
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme

private const val PREFS_NAME = "article_prefs"
private const val KEY_READ = "second_article_read"

private fun isSecondArticleRead(context: Context): Boolean =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_READ, false)

@Composable
fun MainActivityScreen() {
    val context = LocalContext.current

    // rememberSaveable — значение выживет при перевороте экрана и возвращении с другой Activity.
    var likes by rememberSaveable { mutableIntStateOf(0) }
    var dislikes by rememberSaveable { mutableIntStateOf(0) }

    // Флаг «вторая статья прочитана» — читаем из SharedPreferences.
    var isArticleRead by remember { mutableStateOf(isSecondArticleRead(context)) }

    // Лончер, который перезапускает чтение флага, когда пользователь возвращается из SecondActivity.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // resultCode не важен: в любом случае перечитываем состояние из SharedPreferences.
        isArticleRead = isSecondArticleRead(context)
    }

    DroidPractice1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.article_title)) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // ── Изображение (Coil) ────────────────────────────────
                AsyncImage(
                    model = "https://images8.alphacoders.com/903/thumb-1920-903577.jpg",
                    contentDescription = "Иллюстрация к статье",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(Modifier.height(16.dp))

                // ── Подзаголовок (headline) ──────────────────────────
                Text(
                    text = stringResource(R.string.article_subtitle),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                // ── Лид-абзац (titleLarge, курсив) ───────────────────
                Text(
                    text = stringResource(R.string.article_lead),
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                // ── Основной текст (bodyLarge) ───────────────────────
                Text(
                    text = stringResource(R.string.article_body),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )

                Spacer(Modifier.height(20.dp))

                // ── Цитата в Card ────────────────────────────────────
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.article_quote),
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // ── Кнопка «Поделиться» ──────────────────────────────
                Button(
                    onClick = {
                        val articleText = buildString {
                            append(context.getString(R.string.article_title))
                            append("\n\n")
                            append(context.getString(R.string.article_lead))
                            append("\n\n")
                            append(context.getString(R.string.article_body))
                        }
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.article_title))
                            putExtra(Intent.EXTRA_TEXT, articleText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.btn_share))
                }

                Spacer(Modifier.height(16.dp))

                // ── Лайки / дизлайки ─────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilledTonalButton(
                        onClick = { likes++ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${stringResource(R.string.btn_like)} $likes")
                    }
                    OutlinedButton(
                        onClick = { dislikes++ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${stringResource(R.string.btn_dislike)} $dislikes")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Плашка «Прочитано» (если вторая статья прочитана) ─
                if (isArticleRead) {
                    Text(
                        text = stringResource(R.string.read_badge),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(8.dp))

                // ── Переход на вторую статью ─────────────────────────
                Button(
                    onClick = {
                        val intent = Intent(context, SecondActivity::class.java)
                        launcher.launch(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.btn_next_article))
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}