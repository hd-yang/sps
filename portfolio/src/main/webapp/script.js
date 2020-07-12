// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function addRandomQuote() {
    const quotes =
    [
        "Life was like a box of chocolates, you never know what you're gonna get.",
        "Get busy living or get busy dying.",
        "With great power there must come great responsibility.",
        "May the Force be with you.",
        "I'll be back.",
        "You jump,I jump.",
        "In case I don't see ya', good afternoon, good evening and goodnight."
    ];

    const quote = quotes[Math.floor(Math.random() * quotes.length)];

    const quoteContainer = document.getElementById('quote-container');
    quoteContainer.innerText = quote;
}

function pickAGame() {
    const games = [
        ["Don't Starve", "ds.jpg"],
        ["Dead Cells", "dss.jpg"],
        ["Soul Knight", "yqqs.jpg"],
        ["Hollow Knight", "hk.jpg"],
        ["Terraria", "tlry.jpg"],
        ["Brawl Stars", "bs.jpg"],
        ["Ori and the Blind Forest", "ori.jpg"]
    ];

    const game = games[Math.floor(Math.random() * games.length)];
    const img_url = "<img src=\"/images/" + game[1] + "\">";

    const gameContainer = document.getElementById('game-container');
    const gameImg = document.getElementById('game-img');
    gameContainer.innerText = "Do you like <" + game[0] + "> ?";
    gameImg.innerHTML = img_url;
}
