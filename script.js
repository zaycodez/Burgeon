const welcomeScreen = document.getElementById("welcome-screen");
const forestScreen = document.getElementById("forest-screen");
const shopPanel = document.getElementById("shop-panel");
const seedDisplay = document.getElementById("seeds");
const shopMessage = document.getElementById("shop-message");
const plantArea = document.getElementById("plant-area");

let seeds = 100;
let plantCount = 0;

document.getElementById("exploreButton").onclick = function() {
    welcomeScreen.classList.add("hidden");
    forestScreen.classList.remove("hidden");
};

document.getElementById("shop-button").onclick = function() {
    shopPanel.classList.toggle("hidden");
};

document.getElementById("close-shop").onclick = function() {
    shopPanel.classList.add("hidden");
};

function buyPlant(type, cost, emoji) {
    if (seeds < cost) {
        shopMessage.textContent = "Not enough seeds! 🌱";
        return;
    }

    seeds -= cost;
    seedDisplay.textContent = seeds;

    const plant = document.createElement("div");
    plant.className = "plant";
    plant.textContent = emoji;

    plant.style.left = (25 + (plantCount % 5) * 12) + "%";
    plant.style.top = (35 + Math.floor(plantCount / 5) * 12) + "%";

    plantArea.appendChild(plant);
    plantCount++;

    shopMessage.textContent = "You planted a " + type + "! 🌿";
}

document.getElementById("buy-flower").onclick = function() {
    buyPlant("flower", 10, "🌸");
};

document.getElementById("buy-tree").onclick = function() {
    buyPlant("tree", 25, "🌳");
};