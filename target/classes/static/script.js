// 必要な要素を取得
const logContent = document.getElementById("log-content");
const hpElement = document.getElementById("hp");
const attackElement = document.getElementById("attack");
const levelElement = document.getElementById("level");
const characterImage = document.getElementById("character-image");
const directionElement = document.getElementById("direction");
const choiceElement = document.getElementById("choice");

const LEVEL_UPEXP = 30; // レベルアップする経験値の量
const LEVEL_UP_ATK = 20; // レベルアップで加算される攻撃力
const LEVEL_UP_HP = 20; // レベルアップで加算されるMAXHPの量

const APPEARANCE_RATE = 0.4; // モンスター出現率40％
const ESCAPING_RATE = 0.5; // 逃げれる確率

// 主人公データ
const hero = {
  hp: 100,
  maxHp: 100,
  attack: 10,
  level: 1,
  exp: 0,
  image: "image/yusha.png",
};
// モンスターのデータ
const MONSTERS = [
  {
    name: "スライム",
    hp: 20,
    attack: 10,
    exp: 10,
    image: "image/Slime.webp",
    rate: 0.65, // 出現確率
  },
  {
    name: "ドラゴン",
    hp: 40,
    attack: 20,
    exp: 15,
    image: "image/dragon.webp",
    rate: 0.25, // 出現確率
  },
  {
    name: "メタルスライム",
    hp: 20,
    attack: 10,
    exp: 30,
    image: "image/Metal_slime.webp",
    rate: 0.1, // 出現確率
  },
];
// ゲーム変数
let currentMonster = null;
// ステータス更新
const updateStatus = () => {
  hpElement.textContent = hero.hp;
  attackElement.textContent = hero.attack;
  levelElement.textContent = hero.level;
};

// キャラクター画像を更新
const updateCharacterImage = (imagePath) => {
  characterImage.src = imagePath;
};

// コントロール切り替え
const toggleControls = (isDirectionActive) => {
  if (isDirectionActive === true) {
    directionElement.style.display = "flex"; // 十字キーを表示
    choiceElement.style.display = "none"; // ボタンを非表示
    updateCharacterImage(hero.image); // 非戦闘中はヒーロー画像を表示
  } else {
    directionElement.style.display = "none"; // 十字キーを非表示
    choiceElement.style.display = "flex"; // ボタンを表示
  }
};

// ログを追加
const addLog = (message) => {
  const logLine = document.createElement("p");
  logLine.textContent = message;
  logContent.appendChild(logLine);
  logContent.scrollTop = logContent.scrollHeight;
};

// ゲームを初期化
const resetGame = () => {
  hero.hp = 100; // 初期HP
  hero.maxHp = 100; // 初期最大HP
  hero.attack = 10; // 初期攻撃力
  hero.level = 1; // 初期レベル
  hero.exp = 0; // 初期経験値
  currentMonster = null;
  updateCharacterImage(hero.image);
  toggleControls(true);
  updateStatus();
};

// レベルアップ判定
const checkLevelUp = () => {
  while (hero.exp >= LEVEL_UPEXP) { // 複数回のレベルアップに対応
    hero.exp -= LEVEL_UPEXP;
    hero.level ++;
    hero.maxHp += LEVEL_UP_ATK;
    hero.attack += LEVEL_UP_HP;
    hero.hp = hero.maxHp; // HPを全回復
    addLog(`レベルアップ！ レベル: ${hero.level}、HP: ${hero.hp}、攻撃力: ${hero.attack}`);
  }
};

// モンスターを選択するロジック
const chooseMonster = () => {
  const random = Math.random();
  let cumulativeRate = 0;
  for (const monster of MONSTERS) {
    cumulativeRate += monster.rate;
    if (random <= cumulativeRate) {
      return { ...monster }; // モンスターのコピーを返す（HPの変更に対応）
    }
  }
  return { ...MONSTERS[MONSTERS.length - 1] }; // デフォルトで最後のモンスターを返す（安全策）
};

// モンスターの攻撃処理
const monsterAttack = (monster) => {
  hero.hp -= monster.attack; // ヒーローのHPを減らす
  if (hero.hp <= 0) {
    hero.hp = 0; // HPが0以下にならないよう制御
    updateStatus();
    addLog(`${monster.name} の攻撃！ ヒーローは倒れました...`);
    showGameOverPopup(); // ポップアップを表示
  } else {
    addLog(`${monster.name} の攻撃！ ヒーローは${monster.attack}のダメージを受けた！`);
    updateStatus(); // HPを更新
  }
};

// 戦闘処理
const startBattle = (monster) => {
  addLog(`${monster.name}と戦闘が始まりました！`);
  updateCharacterImage(monster.image); // 戦闘中はモンスター画像を表示
  currentMonster(false)
};

// ヒーローの攻撃処理
const attackMonster = () => {
  if (!currentMonster) return;

  currentMonster.hp -= hero.attack; // モンスターのHPを減らす
  addLog(`ヒーローの攻撃！ ${currentMonster.name} に${hero.attack}のダメージ！`);

  if (currentMonster.hp <= 0) {
    addLog(`${currentMonster.name} を倒した！ 経験値: ${currentMonster.exp} 獲得！`);
    hero.exp += currentMonster.exp; // 経験値を加算
    checkLevelUp(); // レベルアップ判定
    toggleControls(true); // 初期状態に戻す
    updateCharacterImage(hero.image); // ヒーロー画像に戻す
    currentMonster = null; // モンスターをリセット
    updateStatus(); // ステータス更新
  } else {
    monsterAttack(currentMonster); // モンスターの反撃
  }
};

// エンカウント判定 (40%)
const encounterMonster = () => {
  const encounterChance = Math.random();
  console.log(encounterChance);
  if (encounterChance < APPEARANCE_RATE) {
    currentMonster = chooseMonster(); // モンスターを選択
    updateCharacterImage(currentMonster.image); // モンスター画像を表示
    addLog(`${currentMonster.name} が現れた！`);
    toggleControls(false); // モンスターがいるとき
    startBattle(currentMonster); // 戦闘開始
  } else {
    addLog("何も見つかりませんでした。");
    toggleControls(true); // 非戦闘状態
    updateCharacterImage(hero.image); // ヒーロー画像を表示
  }
};

// 移動処理
const moveHero = (direction) => {
  addLog(`${direction}方向に進みました。`);
  encounterMonster();
};

// 逃げる処理
const tryToEscape = () => {
  const escapeChance = Math.random();
  if (escapeChance < ESCAPING_RATE) {
    addLog("うまく逃げ切れました！");
    toggleControls(true); // 初期画面に戻す
    updateCharacterImage(hero.image); // ヒーロー画像に戻す
    currentMonster = null; // 現在のモンスターをリセット
  } else {
    addLog("逃げられませんでした！");
    console.log(currentMonster)
    monsterAttack(currentMonster); // モンスターが攻撃
  }
};

// ポップアップ表示
const showGameOverPopup = () => {
  const popup = document.createElement("div");
  popup.id = "game-over-popup";
  popup.style = `
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    padding: 20px;
    background-color: #fff;
    border: 2px solid #000;
    z-index: 1000;
    text-align: center;
  `;

  const message = document.createElement("p");
  message.textContent = "主人公が死にました。\n蘇生しますか？";
  popup.appendChild(message);

  const yesButton = document.createElement("button");
  yesButton.textContent = "YES";
  yesButton.style = `
    margin-top: 10px;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
  `;
  yesButton.addEventListener("click", () => {
    document.body.removeChild(popup); // ポップアップを削除
    resetGame(); // ゲーム初期化
    addLog("主人公が蘇生しました。");
  });

  popup.appendChild(yesButton);
  document.body.appendChild(popup);
};

// イベントハンドラーの登録
window.onload = () => resetGame();
document.getElementById("up").addEventListener("click", () => moveHero("上"));
document.getElementById("down").addEventListener("click", () => moveHero("下"));
document.getElementById("left").addEventListener("click", () => moveHero("左"));
document.getElementById("right").addEventListener("click", () => moveHero("右"));
document.getElementById("Start-fighting").addEventListener("click", () => attackMonster());
document.getElementById("run-away").addEventListener("click", () => tryToEscape());
