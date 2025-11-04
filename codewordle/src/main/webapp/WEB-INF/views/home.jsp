<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeWordle - Inicio</title>
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

            .text-gradient {
                background: linear-gradient(135deg, #ffffff 0%, #f0f0f0 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                background-clip: text;
            }

            .animate-fade-in {
                animation: fadeIn 0.6s ease-out;
            }

            .animate-slide-up {
                animation: slideUp 0.5s ease-out;
            }
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>
<body class="min-h-screen">
    <div class="container mx-auto px-4 py-12 relative z-10">
        <!-- Header -->
        <header class="text-center mb-16 animate-fade-in">
            <h1 class="text-6xl md:text-7xl font-bold mb-6 text-gradient drop-shadow-lg">
                CODEWORDLE
            </h1>
            <p class="text-2xl md:text-3xl text-white font-semibold tracking-wide drop-shadow-md">
                Un Juego de Palabras con Código
            </p>
            <p class="mt-4 text-white/80 text-lg">
                Adivina términos de programación en 6 intentos
            </p>
        </header>

        <!-- Theme Selection -->
        <div class="max-w-4xl mx-auto mb-12 animate-slide-up">
            <div class="glass-strong rounded-3xl p-8 md:p-10 shadow-2xl">
                <h2 class="text-3xl md:text-4xl font-bold text-center mb-10 text-white drop-shadow-md">
                    Selecciona tu Tema
                </h2>

                <form action="/start-game" method="post" id="theme-form">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                        <c:forEach var="theme" items="${themes}" varStatus="status">
                            <label class="cursor-pointer block group">
                                <input type="radio" name="themeId" value="${theme.id}" class="peer hidden" required>
                                <div class="glass rounded-2xl p-6 transition-all duration-300
                                            hover:scale-105 hover:shadow-xl hover:bg-white/20
                                            peer-checked:bg-white/25 peer-checked:border-white peer-checked:shadow-2xl
                                            peer-checked:ring-4 peer-checked:ring-white/50">
                                    <div class="text-5xl mb-4 text-center filter drop-shadow-lg">
                                        <c:choose>
                                            <c:when test="${theme.name == 'JAVA'}">
                                                <svg class="w-16 h-16 mx-auto text-white" fill="currentColor" viewBox="0 0 24 24">
                                                    <path d="M8.851 18.56s-.917.534.653.714c1.902.218 2.874.187 4.969-.211 0 0 .552.346 1.321.646-4.699 2.013-10.633-.118-6.943-1.149M8.276 15.933s-1.028.761.542.924c2.032.209 3.636.227 6.413-.308 0 0 .384.389.987.602-5.679 1.661-12.007.13-7.942-1.218M13.116 11.475c1.158 1.333-.304 2.533-.304 2.533s2.939-1.518 1.589-3.418c-1.261-1.772-2.228-2.652 3.007-5.688 0-.001-8.216 2.051-4.292 6.573M19.33 20.504s.679.559-.747.99c-2.712.822-11.288 1.069-13.669.033-.856-.373.75-.89 1.254-.998.527-.114.828-.093.828-.093-.953-.671-6.156 1.317-2.643 1.887 9.58 1.553 17.462-.7 14.977-1.819M9.292 13.21s-4.362 1.036-1.544 1.412c1.189.159 3.561.123 5.77-.062 1.806-.152 3.618-.477 3.618-.477s-.637.272-1.098.587c-4.429 1.165-12.986.623-10.522-.568 2.082-1.006 3.776-.892 3.776-.892M17.116 17.584c4.503-2.34 2.421-4.589.968-4.285-.355.074-.515.138-.515.138s.132-.207.385-.297c2.875-1.011 5.086 2.981-.928 4.562 0-.001.07-.063.09-.118M14.401 0s2.494 2.494-2.365 6.33c-3.896 3.077-.888 4.832-.001 6.836-2.274-2.053-3.943-3.858-2.824-5.539 1.644-2.469 6.197-3.665 5.19-7.627M9.734 23.924c4.322.277 10.959-.153 11.116-2.198 0 0-.302.775-3.572 1.391-3.688.694-8.239.613-10.937.168 0-.001.553.457 3.393.639"/>
                                                </svg>
                                            </c:when>
                                            <c:when test="${theme.name == 'SPRING'}">
                                                <svg class="w-16 h-16 mx-auto text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                                                </svg>
                                            </c:when>
                                            <c:when test="${theme.name == 'DEVOPS'}">
                                                <svg class="w-16 h-16 mx-auto text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"/>
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                                                </svg>
                                            </c:when>
                                            <c:when test="${theme.name == 'DATABASE'}">
                                                <svg class="w-16 h-16 mx-auto text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4"/>
                                                </svg>
                                            </c:when>
                                            <c:otherwise>
                                                <svg class="w-16 h-16 mx-auto text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/>
                                                </svg>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <h3 class="text-2xl font-bold mb-2 text-center text-white">
                                        ${theme.name}
                                    </h3>
                                    <p class="text-center text-white/90 text-sm">
                                        ${theme.description}
                                    </p>
                                </div>
                            </label>
                        </c:forEach>
                    </div>

                    <button type="submit"
                            class="w-full glass-strong text-white text-xl font-bold py-5 px-8 rounded-2xl
                                   transition-all duration-300 hover:bg-white/30 hover:scale-105 hover:shadow-2xl
                                   active:scale-95 uppercase tracking-wide">
                        ▶ Iniciar Juego
                    </button>
                </form>
            </div>
        </div>

        <!-- Game Instructions -->
        <div class="max-w-4xl mx-auto animate-slide-up" style="animation-delay: 0.2s;">
            <div class="glass-strong rounded-3xl p-8 md:p-10 shadow-2xl">
                <h3 class="text-3xl font-bold text-center mb-8 text-white drop-shadow-md">
                    Cómo Jugar
                </h3>

                <div class="space-y-4">
                    <div class="flex items-start gap-4 p-4 glass rounded-xl hover:bg-white/15 transition-all">
                        <div class="flex-shrink-0 w-12 h-12 rounded-full bg-white/20 flex items-center justify-center text-white font-bold text-lg border-2 border-white/30">
                            1
                        </div>
                        <p class="text-white text-lg pt-2">
                            Adivina la palabra relacionada con programación en <span class="font-bold">6 intentos</span>
                        </p>
                    </div>

                    <div class="flex items-start gap-4 p-4 glass rounded-xl hover:bg-white/15 transition-all">
                        <div class="flex-shrink-0 w-12 h-12 rounded-full bg-green-500/60 flex items-center justify-center text-white font-bold text-lg border-2 border-green-400/50 shadow-lg">
                            2
                        </div>
                        <p class="text-white text-lg pt-2">
                            Las letras en <span class="font-bold text-green-300">VERDE</span> están en la posición correcta
                        </p>
                    </div>

                    <div class="flex items-start gap-4 p-4 glass rounded-xl hover:bg-white/15 transition-all">
                        <div class="flex-shrink-0 w-12 h-12 rounded-full bg-amber-500/60 flex items-center justify-center text-white font-bold text-lg border-2 border-amber-400/50 shadow-lg">
                            3
                        </div>
                        <p class="text-white text-lg pt-2">
                            Las letras en <span class="font-bold text-amber-300">AMARILLO</span> están en la palabra pero en otra posición
                        </p>
                    </div>

                    <div class="flex items-start gap-4 p-4 glass rounded-xl hover:bg-white/15 transition-all">
                        <div class="flex-shrink-0 w-12 h-12 rounded-full bg-gray-500/40 flex items-center justify-center text-white font-bold text-lg border-2 border-gray-400/30">
                            4
                        </div>
                        <p class="text-white text-lg pt-2">
                            Las letras en <span class="font-bold text-gray-300">GRIS</span> no están en la palabra
                        </p>
                    </div>

                    <div class="flex items-start gap-4 p-4 glass rounded-xl hover:bg-white/15 transition-all">
                        <div class="flex-shrink-0 w-12 h-12 rounded-full bg-blue-500/60 flex items-center justify-center text-white font-bold text-lg border-2 border-blue-400/50 shadow-lg">
                            5
                        </div>
                        <p class="text-white text-lg pt-2">
                            Todas las palabras válidas tienen <span class="font-bold">5 letras</span>
                        </p>
                    </div>
                </div>

                <div class="mt-8 p-6 glass rounded-xl border-l-4 border-amber-400">
                    <p class="text-white/90 text-base">
                        <span class="font-bold text-amber-300">💡 Consejo:</span>
                        ¡Piensa en términos comunes de programación del tema seleccionado!
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Store original button text
        const originalButtonText = '▶ Iniciar Juego';

        // Enhance form submission
        document.getElementById('theme-form').addEventListener('submit', function(e) {
            const button = this.querySelector('button[type="submit"]');
            button.innerHTML = '⌛ Iniciando...';
            button.style.opacity = '0.7';
            button.disabled = true;
        });

        // Reset button state when page is shown (fixes back button issue)
        window.addEventListener('pageshow', function(event) {
            const form = document.getElementById('theme-form');
            const button = form.querySelector('button[type="submit"]');
            button.innerHTML = originalButtonText;
            button.style.opacity = '1';
            button.disabled = false;
        });

        // Add stagger animation to theme cards
        document.addEventListener('DOMContentLoaded', () => {
            const themeCards = document.querySelectorAll('.grid > label');
            themeCards.forEach((card, index) => {
                card.style.opacity = '0';
                card.style.animation = `slideUp 0.5s ease-out ${index * 0.1}s forwards`;
            });
        });
    </script>
</body>
</html>
