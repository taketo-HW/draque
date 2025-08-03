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
  toggleControls(false);
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

// エンカウント判定
async function encounterMonster() {
  try {
    const res = await fetch('/api/encounter');
    const data = await res.json();
    if (data.appear) {
      currentMonster = { 
        name: data.monster.name,
        hp: data.monster.hp,
        attack: data.monster.attack,
        exp: data.monster.exp,
        image: data.monster.image
      };
      addLog(`${currentMonster.name} が現れた！`);
      updateCharacterImage(currentMonster.image);
      toggleControls(false);
      startBattle(currentMonster);
    } else {
      addLog("何も見つかりませんでした。");
      toggleControls(true);
      updateCharacterImage(hero.image);
    }
  } catch (err) {
    addLog(`出現判定エラー: ${err}`);
  }
}

// 移動処理
const moveHero = (direction) => {
  addLog(`${direction}方向に進みました。`);
  
  // 現在位置が取得できている場合、マップ上のポイントも移動
  if (currentPosition && positionMarker && currentCircle) {
    const newPosition = calculateNewPosition(currentPosition, direction);
    currentPosition = newPosition;
    
    // マーカーと円の位置を更新
    positionMarker.setLatLng(newPosition);
    currentCircle.setLatLng(newPosition);
    
    // マップの中心も移動
    map.setView(newPosition, 13);
  }
  
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
    console.log(currentMonster);
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

// Leaflet 関連の変数
let map;
let currentPosition = null;
let positionMarker = null;
let currentCircle = null;

// 移動距離の計算（緯度経度の差分）
const calculateNewPosition = (currentPos, direction) => {
  const latDiff = 0.001; // 約100メートル
  const lngDiff = 0.001; // 約100メートル
  
  switch(direction) {
    case "上":
      return [currentPos[0] + latDiff, currentPos[1]];
    case "下":
      return [currentPos[0] - latDiff, currentPos[1]];
    case "左":
      return [currentPos[0], currentPos[1] - lngDiff];
    case "右":
      return [currentPos[0], currentPos[1] + lngDiff];
    default:
      return currentPos;
  }
};

// Leaflet の初期化
function initMap() {
  // 東京駅の位置
  const tokyoStationPosition = [35.6812, 139.7671];
  
  map = L.map('map', {
    zoomControl: false, // ズームコントロールを無効化
    scrollWheelZoom: false, // マウスホイールでのズームを無効化
    doubleClickZoom: false, // ダブルクリックでのズームを無効化
    boxZoom: false, // ボックスズームを無効化
    keyboard: false, // キーボードでのズームを無効化
    dragging: false, // ドラッグでの移動を無効化
    touchZoom: false, // タッチでのズームを無効化
    bounceAtZoomLimits: false // ズーム制限でのバウンスを無効化
  }).setView(tokyoStationPosition, 13);

  // 複数のタイルプロバイダーを試行
  const tileProviders = [
    'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
    'https://a.tile.openstreetmap.org/{z}/{x}/{y}.png',
    'https://b.tile.openstreetmap.org/{z}/{x}/{y}.png',
    'https://c.tile.openstreetmap.org/{z}/{x}/{y}.png'
  ];

  let tileLayerAdded = false;
  
  // タイルレイヤーを順番に試行
  for (let i = 0; i < tileProviders.length && !tileLayerAdded; i++) {
    try {
      const tileLayer = L.tileLayer(tileProviders[i], {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19,
        timeout: 5000
      });
      
      tileLayer.on('load', function() {
        console.log('タイルレイヤーが正常に読み込まれました:', tileProviders[i]);
      });
      
      tileLayer.on('tileerror', function() {
        console.log('タイルレイヤーエラー:', tileProviders[i]);
      });
      
      tileLayer.addTo(map);
      tileLayerAdded = true;
    } catch (error) {
      console.log('タイルレイヤーの追加に失敗:', tileProviders[i], error);
    }
  }

  // タイルレイヤーが追加されなかった場合のフォールバック
  if (!tileLayerAdded) {
    console.log('すべてのタイルプロバイダーに失敗しました。シンプルな地図を表示します。');
    // シンプルな背景色で地図エリアを表示
    document.getElementById('map').style.backgroundColor = '#e0e0e0';
    document.getElementById('map').innerHTML = '<div style="text-align: center; padding-top: 50px; color: #666;">地図を読み込めませんでした<br>東京駅周辺の地図</div>';
  }

  // 東京駅のマーカーと円を表示
  const tokyoStationMarker = L.marker(tokyoStationPosition, {
    title: "東京駅"
  }).addTo(map);

  const tokyoStationCircle = L.circle(tokyoStationPosition, {
    color: 'red',
    fillColor: '#f03',
    fillOpacity: 0.1,
    radius: 500 // 500メートル
  }).addTo(map);

  // 現在位置を取得
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        currentPosition = [position.coords.latitude, position.coords.longitude];
        
        // 東京駅のマーカーと円を削除
        map.removeLayer(tokyoStationMarker);
        map.removeLayer(tokyoStationCircle);
        
        // マップの中心を現在位置に設定
        map.setView(currentPosition, 13);
        
        // 現在位置のマーカーを追加
        positionMarker = L.marker(currentPosition, {
          title: "現在位置"
        }).addTo(map);

        // 500m四方の範囲を表示（円形）
        currentCircle = L.circle(currentPosition, {
          color: 'red',
          fillColor: '#f03',
          fillOpacity: 0.1,
          radius: 500 // 500メートル
        }).addTo(map);

        addLog("現在位置を取得しました。");
      },
      (error) => {
        console.error("位置情報の取得に失敗しました:", error);
        addLog("位置情報の取得に失敗しました。東京駅を中心とした地図を表示しています。");
        
        // エラーメッセージに応じた説明を追加
        switch(error.code) {
          case error.PERMISSION_DENIED:
            addLog("位置情報の許可が拒否されました。東京駅周辺の地図を表示しています。");
            break;
          case error.POSITION_UNAVAILABLE:
            addLog("位置情報が利用できません。東京駅周辺の地図を表示しています。");
            break;
          case error.TIMEOUT:
            addLog("位置情報の取得がタイムアウトしました。東京駅周辺の地図を表示しています。");
            break;
          default:
            addLog("位置情報の取得でエラーが発生しました。東京駅周辺の地図を表示しています。");
        }
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 60000
      }
    );
  } else {
    addLog("このブラウザは位置情報をサポートしていません。東京駅周辺の地図を表示しています。");
  }
}

// イベントハンドラーの登録
window.onload = () => {
  resetGame();
  initMap();
};
document.getElementById("up").addEventListener("click", () => moveHero("上"));
document.getElementById("down").addEventListener("click", () => moveHero("下"));
document.getElementById("left").addEventListener("click", () => moveHero("左"));
document.getElementById("right").addEventListener("click", () => moveHero("右"));
document.getElementById("Start-fighting").addEventListener("click", () => attackMonster());
document.getElementById("run-away").addEventListener("click", () => tryToEscape());
