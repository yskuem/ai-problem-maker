package app.yskuem.aimondaimaker.feature.note.ui

import PastelAppleStyleLoading
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.feature.quiz.ui.QuizApp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

data class CreateNoteScreen(
    val imageByte: ByteArray,
    val fileName: String = "image",
    val extension: String
): Screen {
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowNoteScreenViewModel> ()
        val state by viewmodel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewmodel.onLoadPage(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension
            )
        }

        when(val note = state.note) {
            is DataUiState.Error -> {
                Text(note.throwable.toString())
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading()
            }
            is DataUiState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val htmlState = rememberWebViewStateWithHTMLData(note.data.html)
                    WebView(
                        state = htmlState,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreateNoteScreen

        if (!imageByte.contentEquals(other.imageByte)) return false
        if (fileName != other.fileName) return false
        if (extension != other.extension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageByte.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + extension.hashCode()
        return result
    }

}

const val baseHtml = """
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>洗練されたノートまとめページ</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;500;700&family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        /* グローバルスタイルとリセット */
        *, *::before, *::after {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Noto Sans JP', sans-serif;
            background-color: #f9f9f9; /* より明るい背景 */
            color: #333;
            line-height: 1.7;
            -webkit-font-smoothing: antialiased;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 30px;
        }

        /* ヘッダー */
        header {
            background-color: #fff;
            padding: 70px 20px;
            text-align: center;
            border-bottom: 1px solid #e9ecef; /* より薄いボーダー */
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03); /* より繊細なシャドウ */
            margin-bottom: 50px;
        }

        header h1 {
            font-family: 'Poppins', sans-serif; /* モダンなフォント */
            font-size: 3.5em;
            color: #212529; /* より濃いグレー */
            margin-bottom: 20px;
            letter-spacing: -1px;
        }

        header p {
            font-size: 1.2em;
            color: #6c757d; /* より柔らかなグレー */
            max-width: 750px;
            margin: 0 auto;
        }

        /* ノートカードセクション */
        .notes-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 35px;
            padding-bottom: 70px;
        }

        .note-card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.06); /* より洗練されたシャドウ */
            padding: 35px;
            transition: transform 0.25s ease, box-shadow 0.25s ease;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-height: 290px;
        }

        .note-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .note-card h3 {
            font-family: 'Poppins', sans-serif;
            font-size: 2em;
            color: #343a40; /* わずかに濃いグレー */
            margin-bottom: 18px;
            line-height: 1.4;
        }

        .note-card p {
            font-size: 1.05em;
            color: #495057; /* 少し明るいグレー */
            margin-bottom: 25px;
            flex-grow: 1;
        }

        .note-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 0.95em;
            color: #868e96; /* 落ち着いたグレー */
            border-top: 1px solid #e9ecef;
            padding-top: 18px;
            margin-top: 20px;
        }

        .note-meta .date {
            font-weight: 500;
            color: #495057;
        }

        .note-meta .tags {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .note-meta .tag {
            background-color: #eff2f7; /* より淡い背景 */
            color: #5c6bc0; /* インディゴ */
            padding: 6px 14px;
            border-radius: 22px;
            font-size: 0.85em;
            transition: background-color 0.2s ease;
        }

        .note-meta .tag:hover {
            background-color: #e0e5ec;
        }

        .read-more {
            display: inline-flex;
            align-items: center;
            background-color: #5c6bc0; /* インディゴ */
            color: #fff;
            padding: 14px 28px;
            border-radius: 10px;
            text-decoration: none;
            margin-top: 30px;
            font-weight: 500;
            transition: background-color 0.3s ease, transform 0.2s ease;
            align-self: flex-start;
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.08); /* 軽いシャドウ */
        }

        .read-more:hover {
            background-color: #4a56a2;
            transform: translateY(-3px);
        }

        .read-more svg {
            width: 18px;
            height: 18px;
            margin-left: 8px;
            fill: #fff;
            transition: transform 0.3s ease;
        }

        .read-more:hover svg {
            transform: translateX(5px);
        }

        /* フッター */
        footer {
            background-color: #343a40;
            color: #fff;
            text-align: center;
            padding: 35px 20px;
            font-size: 0.95em;
            border-top: 1px solid #4a5568;
        }

        /* レスポンシブデザイン */
        @media (max-width: 768px) {
            header h1 {
                font-size: 2.8em;
            }

            header p {
                font-size: 1.1em;
            }

            .note-card {
                padding: 30px;
                min-height: auto;
            }

            .note-card h3 {
                font-size: 1.7em;
            }

            .note-card p {
                font-size: 1em;
            }

            .note-meta {
                flex-direction: column;
                align-items: flex-start;
                gap: 12px;
            }

            .read-more {
                width: 100%;
                text-align: center;
                padding: 12px 24px;
            }

            .read-more svg {
                display: none; /* モバイルではアイコンを非表示 */
            }
        }

        @media (max-width: 480px) {
            header {
                padding: 50px 15px;
            }

            header h1 {
                font-size: 2.3em;
            }

            .container {
                padding: 20px;
            }

            .notes-grid {
                gap: 25px;
            }

            .note-card {
                border-radius: 10px;
                padding: 25px;
            }
        }
    </style>
</head>
<body>
    <header>
        <h1> My Notes</h1>
        <p>日々の学びやアイデアを、美しく整理して共有するためのノートまとめページです。技術的なメモから個人的な思考まで、幅広いテーマのノートを閲覧できます。</p>
    </header>

    <main class="container">
        <div class="notes-grid">
            <!-- ノートカード1 -->
            <div class="note-card">
                <div>
                    <h3>CSS Grid Layoutマスターガイド</h3>
                    <p>CSS Grid Layoutの強力な機能を活用して、複雑なWebレイアウトを簡単に作成する方法を学びます。レスポンシブデザインにも対応。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年10月26日</span>
                    <div class="tags">
                        <span class="tag">#WebDesign</span>
                        <span class="tag">#CSS</span>
                        <span class="tag">#Frontend</span>
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>

            <!-- ノートカード2 -->
            <div class="note-card">
                <div>
                    <h3>React Hooks徹底解説</h3>
                    <p>React Hooksを使って、関数コンポーネントで状態管理やライフサイクル機能を扱う方法を詳しく解説します。useState, useEffectなど。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年10月20日</span>
                    <div class="tags">
                        <span class="tag">#React</span>
                        <span class="tag">#JavaScript</span>
                        <span class="tag">#Frontend</span>
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>

            <!-- ノートカード3 -->
            <div class="note-card">
                <div>
                    <h3>アクセシブルなWeb開発</h3>
                    <p>すべてのユーザーが利用しやすいWebサイトを構築するためのアクセシビリティの基本原則と具体的な実装方法を紹介します。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年10月15日</span>
                    <div class="tags">
                        <span class="tag">#Accessibility</span>
                        <span class="tag">#WebDev</span>
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>

            <!-- ノートカード4 -->
            <div class="note-card">
                <div>
                    <h3>RESTful APIデザインのベストプラクティス</h3>
                    <p>効果的で保守しやすいRESTful APIを設計するためのベストプラクティスを紹介します。URI設計、HTTPメソッド、ステータスコードなど。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年10月10日</span>
                    <div class="tags">
                        <span class="tag">#API</span>
                        <span class="tag">#Backend</span>
                        <span class="tag">#WebDev</span>
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>

            <!-- ノートカード5 -->
            <div class="note-card">
                <div>
                    <h3>GraphQL入門</h3>
                    <p>GraphQLの基本概念から、REST APIとの違い、クエリの書き方、クライアントサイドでのデータ取得までを解説します。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年10月05日</span>
                    <div class="tags">
                        <span class="tag">#GraphQL</span>
                        <span class="tag">#API</span>
                        <span class="tag">#WebDev</span>
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>

            <!-- ノートカード6 -->
            <div class="note-card">
                <div>
                    <h3>Dockerコンテナの基本</h3>
                    <p>Dockerコンテナの基本操作、イメージの作成、Dockerfileの書き方、Docker Composeを使った複数コンテナの管理について学びます。</p>
                </div>
                <div class="note-meta">
                    <span class="date">2023年09月28日</span>
                    <div class="tags">
                        <span class="tag">#Docker</span>
                        <span class="tag">#DevOps</span>
                        <span class="tag">#Container</span
                    </div>
                </div>
                <a href="#" class="read-more">
                    ノートを読む
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </a>
            </div>
        </div>
    </main>

    <footer>
        <p>© 2023 My Notes. All rights reserved.</p>
    </footer>
</body>
</html>
"""