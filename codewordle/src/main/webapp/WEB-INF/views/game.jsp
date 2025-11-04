<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeWordle - Juego en Progreso</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;800&display=swap" rel="stylesheet">
    <style type="text/tailwindcss">
        @layer base {
            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                background-attachment: fixed;
            }
        }

        @layer utilities {
            .glass {
                background: rgba(255, 255, 255, 0.1);
                backdrop-filter: blur(20px);
                -webkit-backdrop-filter: blur(20px);
                border: 1px solid rgba(255, 255, 255, 0.2);
            }

            .glass-strong {
                background: rgba(255, 255, 255, 0.15);
                backdrop-filter: blur(25px);
                -webkit-backdrop-filter: blur(25px);
                border: 1px solid rgba(255, 255, 255, 0.3);
            }

            .letter-box {
                @apply w-14 h-14 md:w-16 md:h-16 flex items-center justify-center text-2xl md:text-3xl font-bold rounded-xl transition-all duration-300;
                background: rgba(255, 255, 255, 0.15);
                backdrop-filter: blur(10px);
                border: 2px solid rgba(255, 255, 255, 0.3);
                color: white;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                text-align: center;
                padding: 0;
                line-height: 1;
            }

            .letter-box.correct {
                background: linear-gradient(135deg, #10b981 0%, #059669 100%);
                border-color: #10b981;
                animation: flip 0.6s ease, pulse 0.3s ease 0.6s;
            }

            .letter-box.present {
                background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
                border-color: #f59e0b;
                animation: flip 0.6s ease, pulse 0.3s ease 0.6s;
            }

            .letter-box.absent {
                background: rgba(107, 114, 128, 0.5);
                border-color: #6b7280;
                opacity: 0.6;
                animation: flip 0.6s ease;
            }

            .letter-box:focus {
                outline: none;
                border-color: white;
                box-shadow: 0 0 20px rgba(255, 255, 255, 0.4);
                background: rgba(255, 255, 255, 0.2);
            }

            .letter-box.empty {
                opacity: 0.4;
                background: rgba(255, 255, 255, 0.08);
            }
        }

        @keyframes flip {
            0% { transform: rotateX(0deg); }
            50% { transform: rotateX(90deg); }
            100% { transform: rotateX(0deg); }
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
    </style>
</head>
<body class="min-h-screen">
    <div class="container mx-auto px-4 py-6 md:py-8 relative z-10">
        <!-- Header -->
        <header class="text-center mb-6 md:mb-8">
            <h1 class="text-4xl md:text-5xl font-bold mb-4 text-white drop-shadow-lg">
                CODEWORDLE
            </h1>
            <div class="flex flex-wrap justify-center items-center gap-4 text-sm md:text-base">
                <a href="/" class="text-white hover:bg-white/20 px-4 py-2 rounded-lg transition-all font-semibold">
                    🏠 Inicio
                </a>
                <span class="text-white/50">•</span>
                <button onclick="restartGame()" class="text-white hover:bg-white/20 px-4 py-2 rounded-lg transition-all font-semibold">
                    🔄 Nueva Partida
                </button>
            </div>
        </header>

        <!-- Game Board Container -->
        <div class="max-w-2xl mx-auto">
            <!-- Game Status Bar -->
            <div class="glass-strong rounded-2xl p-4 md:p-6 mb-6 shadow-xl">
                <div class="grid grid-cols-3 gap-4 text-center">
                    <div class="space-y-1">
                        <p class="text-white/70 text-xs md:text-sm uppercase tracking-wider font-semibold">Intentos</p>
                        <p class="text-white text-2xl md:text-3xl font-bold" id="attempts-count">${fn:length(attempts)}/6</p>
                    </div>
                    <div class="space-y-1">
                        <p class="text-white/70 text-xs md:text-sm uppercase tracking-wider font-semibold">Estado</p>
                        <c:choose>
                            <c:when test="${gameSession.status == 'WON'}">
                                <span class="inline-block px-4 py-2 bg-green-500/30 border-2 border-green-400 rounded-xl text-white font-bold text-sm md:text-base shadow-lg">
                                    ✓ Victoria
                                </span>
                            </c:when>
                            <c:when test="${gameSession.status == 'LOST'}">
                                <span class="inline-block px-4 py-2 bg-red-500/30 border-2 border-red-400 rounded-xl text-white font-bold text-sm md:text-base shadow-lg">
                                    ✗ Derrota
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="inline-block px-4 py-2 bg-blue-500/30 border-2 border-blue-400 rounded-xl text-white font-bold text-sm md:text-base shadow-lg animate-pulse">
                                    ● En Curso
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="space-y-1">
                        <p class="text-white/70 text-xs md:text-sm uppercase tracking-wider font-semibold">Tema</p>
                        <p class="text-white text-xl md:text-2xl font-bold">
                            <c:choose>
                                <c:when test="${gameSession.themeId == 1}">☕ Java</c:when>
                                <c:when test="${gameSession.themeId == 2}">🌱 Spring</c:when>
                                <c:when test="${gameSession.themeId == 3}">⚙️ DevOps</c:when>
                                <c:when test="${gameSession.themeId == 4}">🗄️ Database</c:when>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>

            <!-- Attempts Display (Previous Guesses) -->
            <div class="glass-strong rounded-2xl p-6 md:p-8 mb-6 shadow-xl">
                <div id="attempts-container" class="space-y-3">
                    <c:forEach var="attempt" items="${attempts}" varStatus="status">
                        <div class="flex justify-center gap-2" data-attempt="${status.index + 1}">
                            <c:forEach var="letter" items="${attempt.feedback}">
                                <div class="letter-box ${letter.status == 'CORRECT' ? 'correct' :
                                          letter.status == 'PRESENT' ? 'present' : 'absent'}">
                                    ${letter.letter}
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>

                    <!-- Empty rows for remaining attempts -->
                    <c:forEach begin="${fn:length(attempts) + 1}" end="6" varStatus="status">
                        <div class="flex justify-center gap-2" data-attempt="${status.index + fn:length(attempts)}">
                            <c:forEach begin="1" end="${wordLength}">
                                <div class="letter-box empty"></div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Current Guess Input (Only show if game is in progress) -->
            <c:if test="${gameSession.status == 'IN_PROGRESS'}">
                <div class="glass-strong rounded-2xl p-6 md:p-8 shadow-xl">
                    <form id="guess-form" class="space-y-6">
                        <div class="flex justify-center gap-2 mb-4" id="input-container">
                            <c:forEach begin="1" end="${wordLength}" varStatus="status">
                                <input type="text"
                                       maxlength="1"
                                       class="letter-box uppercase"
                                       data-index="${status.index - 1}"
                                       oninput="handleInput(this, event)"
                                       onkeydown="handleKeyDown(this, event)"
                                       autocomplete="off"
                                       autocapitalize="characters">
                            </c:forEach>
                        </div>

                        <div class="grid grid-cols-2 gap-3">
                            <button type="button"
                                    onclick="clearInput()"
                                    class="glass text-white font-bold py-4 px-6 rounded-xl
                                           hover:bg-red-500/30 hover:border-red-400 transition-all duration-300
                                           uppercase tracking-wide text-sm md:text-base">
                                ✗ Limpiar
                            </button>
                            <button type="submit"
                                    class="glass text-white font-bold py-4 px-6 rounded-xl
                                           hover:bg-green-500/30 hover:border-green-400 transition-all duration-300
                                           uppercase tracking-wide text-sm md:text-base">
                                ✓ Enviar
                            </button>
                        </div>
                    </form>

                    <!-- Helper Text -->
                    <div class="mt-6 text-center">
                        <p class="text-white/70 text-sm md:text-base">
                            Escribe cualquier palabra de <span class="font-bold text-white">${wordLength} letras</span> y presiona <span class="font-bold text-white">ENTER</span>
                        </p>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Loading Overlay -->
    <div id="loading-overlay" class="hidden fixed inset-0 bg-black/50 backdrop-blur-md flex flex-col items-center justify-center z-50">
        <div class="w-16 h-16 border-4 border-white/30 border-t-white rounded-full animate-spin"></div>
        <p class="text-white text-xl font-semibold mt-4 tracking-wide">Procesando...</p>
    </div>

    <script>
        const gameSessionId = ${gameSession.id};
        const wordLength = ${wordLength};
        const currentAttempts = ${fn:length(attempts)};
        const gameStatus = '${gameSession.status}';

        // Handle input navigation
        function handleInput(input, event) {
            // Convert to uppercase
            input.value = input.value.toUpperCase();

            // Validate input is a letter
            if (input.value && !/^[A-ZÑ]$/.test(input.value)) {
                input.value = '';
                input.style.animation = 'shake 0.3s';
                setTimeout(() => input.style.animation = '', 300);
                return;
            }

            // Move to next input
            if (input.value.length === 1) {
                const next = input.nextElementSibling;
                if (next && next.tagName === 'INPUT') {
                    next.focus();
                }
            }
        }

        // Handle keyboard navigation
        function handleKeyDown(input, event) {
            if (event.key === 'Backspace' && input.value === '') {
                const prev = input.previousElementSibling;
                if (prev && prev.tagName === 'INPUT') {
                    prev.focus();
                    prev.value = '';
                }
            } else if (event.key === 'Enter') {
                event.preventDefault();
                document.getElementById('guess-form').dispatchEvent(new Event('submit'));
            }
        }

        // Clear all inputs
        function clearInput() {
            const inputs = document.querySelectorAll('#input-container input');
            inputs.forEach(input => input.value = '');
            inputs[0].focus();
        }

        // Submit guess via AJAX
        document.getElementById('guess-form')?.addEventListener('submit', async (e) => {
            e.preventDefault();

            // Get submit button and disable it immediately
            const submitButton = e.target.querySelector('button[type="submit"]');
            if (submitButton.disabled) return; // Prevent double submission

            submitButton.disabled = true;
            const originalButtonText = submitButton.innerHTML;
            submitButton.innerHTML = '⌛ Enviando...';
            submitButton.style.opacity = '0.6';

            const inputs = document.querySelectorAll('#input-container input');
            let guess = '';
            inputs.forEach(input => guess += input.value);

            if (guess.length !== wordLength) {
                alert(`Debes ingresar ${wordLength} letras`);
                // Re-enable button on validation error
                submitButton.disabled = false;
                submitButton.innerHTML = originalButtonText;
                submitButton.style.opacity = '1';
                return;
            }

            // Show loading
            document.getElementById('loading-overlay').classList.remove('hidden');

            try {
                const response = await fetch(`/game/${gameSessionId}/guess`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({ guess: guess })
                });

                const result = await response.json();

                // Hide loading
                document.getElementById('loading-overlay').classList.add('hidden');

                if (result.valid) {
                    // Animate the new row with feedback
                    animateNewAttempt(guess, result.feedback);

                    // Wait for animations to complete
                    setTimeout(() => {
                        // Check if game ended
                        if (result.gameWon) {
                            alert('¡Felicidades! ¡Has ganado!');
                            setTimeout(() => window.location.reload(), 1000);
                        } else if (result.gameOver) {
                            alert('¡Juego terminado! Se acabaron los intentos.');
                            setTimeout(() => window.location.reload(), 1000);
                        } else {
                            // Reload page for next attempt
                            window.location.reload();
                        }
                    }, 2000);
                } else {
                    alert(result.message || 'Palabra no válida');
                    // Re-enable button on invalid word
                    submitButton.disabled = false;
                    submitButton.innerHTML = originalButtonText;
                    submitButton.style.opacity = '1';
                }
            } catch (error) {
                document.getElementById('loading-overlay').classList.add('hidden');
                alert('Error al procesar tu respuesta');
                console.error('Error:', error);
                // Re-enable button on error
                submitButton.disabled = false;
                submitButton.innerHTML = originalButtonText;
                submitButton.style.opacity = '1';
            }
        });

        // Animate new attempt
        function animateNewAttempt(guess, feedback) {
            const attemptRow = document.querySelector(`[data-attempt="${currentAttempts + 1}"]`);
            if (!attemptRow) return;

            const letterBoxes = attemptRow.querySelectorAll('.letter-box');

            // Set letters and apply feedback
            letterBoxes.forEach((box, index) => {
                box.textContent = guess[index];
                box.classList.remove('empty');

                const status = feedback[index].status.toLowerCase();
                setTimeout(() => {
                    box.classList.add(status);
                }, index * 100);
            });
        }

        // Restart game
        function restartGame() {
            if (confirm('¿Iniciar una nueva partida?')) {
                window.location.href = '/';
            }
        }

        // Focus first input on page load
        document.addEventListener('DOMContentLoaded', () => {
            const inputs = document.querySelectorAll('#input-container input');
            if (inputs.length > 0) {
                inputs[0].focus();
            }
        });
    </script>
</body>
</html>
