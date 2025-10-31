<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeWordle - Game</title>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen">
    <div class="container mx-auto px-4 py-8">
        <!-- Header -->
        <header class="text-center mb-8">
            <h1 class="text-4xl font-bold text-indigo-800 mb-2">CodeWordle</h1>
            <div class="flex justify-center space-x-4 mb-4">
                <a href="/" class="text-indigo-600 hover:text-indigo-800 transition-colors">Home</a>
                <span class="text-gray-400">|</span>
                <button onclick="restartGame()" class="text-indigo-600 hover:text-indigo-800 transition-colors">New Game</button>
            </div>
        </header>

        <!-- Game Board -->
        <div class="max-w-md mx-auto bg-white rounded-xl shadow-lg p-6 mb-6">
            <!-- Attempts Display -->
            <div id="attempts-container" class="space-y-2 mb-6">
                <c:forEach var="attempt" items="${attempts}" varStatus="status">
                    <div class="flex justify-center space-x-1">
                        <c:forEach var="letter" items="${attempt.feedback}">
                            <div class="w-12 h-12 flex items-center justify-center text-xl font-bold border-2 rounded
                                      ${letter.status == 'CORRECT' ? 'bg-green-500 text-white border-green-500' :
                                        letter.status == 'PRESENT' ? 'bg-yellow-500 text-white border-yellow-500' :
                                        'bg-gray-200 text-gray-700 border-gray-300'}">
                                ${letter.letter}
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>
            </div>

            <!-- Current Guess Input -->
            <form id="guess-form" class="space-y-4">
                <div class="flex justify-center space-x-1 mb-4">
                    <c:forEach begin="1" end="5" varStatus="status">
                        <input type="text"
                               maxlength="1"
                               class="w-12 h-12 text-center text-xl font-bold border-2 border-gray-300 rounded focus:border-indigo-500 focus:outline-none uppercase"
                               oninput="moveToNext(this, event)"
                               onkeydown="handleKeyDown(this, event)">
                    </c:forEach>
                </div>

                <div class="flex justify-center space-x-2">
                    <button type="submit"
                            class="bg-indigo-600 text-white font-semibold py-2 px-6 rounded-lg hover:bg-indigo-700 transition-colors duration-200">
                        Submit Guess
                    </button>
                    <button type="button"
                            onclick="clearInput()"
                            class="bg-gray-600 text-white font-semibold py-2 px-4 rounded-lg hover:bg-gray-700 transition-colors duration-200">
                        Clear
                    </button>
                </div>
            </form>

            <!-- Game Status -->
            <div id="game-status" class="mt-4 text-center">
                <c:choose>
                    <c:when test="${gameSession.status == 'WON'}">
                        <div class="flex items-center justify-center text-green-600 font-semibold text-lg">
                            <svg class="w-6 h-6 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                            </svg>
                            Congratulations! You won!
                        </div>
                    </c:when>
                    <c:when test="${gameSession.status == 'LOST'}">
                        <div class="flex items-center justify-center text-red-600 font-semibold text-lg">
                            <svg class="w-6 h-6 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
                            </svg>
                            Game Over! Try again!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-gray-600">Attempts: ${fn:length(attempts)}/6</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Keyboard -->
        <div class="max-w-md mx-auto bg-white rounded-xl shadow-lg p-4">
            <div class="flex justify-center space-x-1 mb-2">
                <c:forEach var="letter" items="Q,W,E,R,T,Y,U,I,O,P">
                    <button onclick="addLetter('${letter}')"
                            class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                        ${letter}
                    </button>
                </c:forEach>
            </div>
            <div class="flex justify-center space-x-1 mb-2">
                <c:forEach var="letter" items="A,S,D,F,G,H,J,K,L">
                    <button onclick="addLetter('${letter}')"
                            class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                        ${letter}
                    </button>
                </c:forEach>
            </div>
            <div class="flex justify-center space-x-1">
                <button onclick="addLetter('Z')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    Z
                </button>
                <button onclick="addLetter('X')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    X
                </button>
                <button onclick="addLetter('C')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    C
                </button>
                <button onclick="addLetter('V')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    V
                </button>
                <button onclick="addLetter('B')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    B
                </button>
                <button onclick="addLetter('N')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    N
                </button>
                <button onclick="addLetter('M')"
                        class="keyboard-key bg-gray-200 text-gray-800 font-semibold py-2 px-3 rounded hover:bg-gray-300 transition-colors">
                    M
                </button>
                <button onclick="clearInput()"
                        class="bg-red-500 text-white font-semibold py-2 px-3 rounded hover:bg-red-600 transition-colors">
                    ⌫
                </button>
            </div>
        </div>
    </div>

    <script>
        const gameSessionId = ${gameSession.id};

        // Move to next input on character entry
        function moveToNext(input, event) {
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
                }
            }
        }

        // Add letter from virtual keyboard
        function addLetter(letter) {
            const inputs = document.querySelectorAll('#guess-form input[type="text"]');
            for (let input of inputs) {
                if (!input.value) {
                    input.value = letter;
                    moveToNext(input, null);
                    break;
                }
            }
        }

        // Clear all inputs
        function clearInput() {
            const inputs = document.querySelectorAll('#guess-form input[type="text"]');
            inputs.forEach(input => input.value = '');
            inputs[0].focus();
        }

        // Submit guess via AJAX
        document.getElementById('guess-form').addEventListener('submit', async (e) => {
            e.preventDefault();

            const inputs = document.querySelectorAll('#guess-form input[type="text"]');
            let guess = '';
            inputs.forEach(input => guess += input.value);

            if (guess.length !== 5) {
                alert('Please enter a 5-letter word');
                return;
            }

            try {
                const response = await fetch(`/game/${gameSessionId}/guess`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `guess=${guess}`
                });

                const result = await response.json();

                if (result.valid) {
                    // Reload page to show updated attempts
                    window.location.reload();
                } else {
                    alert(result.message);
                }
            } catch (error) {
                alert('Error submitting guess');
                console.error('Error:', error);
            }
        });

        // Restart game
        function restartGame() {
            if (confirm('Start a new game?')) {
                window.location.href = '/';
            }
        }

        // Focus first input on page load
        document.addEventListener('DOMContentLoaded', () => {
            const inputs = document.querySelectorAll('#guess-form input[type="text"]');
            if (inputs.length > 0) {
                inputs[0].focus();
            }
        });
    </script>
</body>
</html>