// 定数定義
const GAME_CONSTANTS = {
  LEVEL_UP_EXP: 30,
  LEVEL_UP_ATK: 20,
  LEVEL_UP_HP: 20,
  APPEARANCE_RATE: 0.4,
  ESCAPING_RATE: 0.5
};

// ログ管理クラス
class LogManager {
  constructor() {
    this.logContent = document.getElementById("log-content");
  }

  addLog(message) {
    const logLine = document.createElement("p");
    logLine.textContent = message;
    this.logContent.appendChild(logLine);
    this.logContent.scrollTop = this.logContent.scrollHeight;
  }
}

// 主人公クラス
class Hero {
  constructor() {
    this.hp = 100;
    this.maxHp = 100;
    this.attack = 10;
    this.level = 1;
    this.exp = 0;
    this.image = "image/yusha.png";
  }

  reset() {
    this.hp = 100;
    this.maxHp = 100;
    this.attack = 10;
    this.level = 1;
    this.exp = 0;
  }

  takeDamage(damage) {
    this.hp = Math.max(0, this.hp - damage);
    return this.hp <= 0;
  }

  gainExp(exp) {
    this.exp += exp;
    return this.checkLevelUp();
  }

  checkLevelUp() {
    let leveledUp = false;
    while (this.exp >= GAME_CONSTANTS.LEVEL_UP_EXP) {
      this.exp -= GAME_CONSTANTS.LEVEL_UP_EXP;
      this.level++;
      this.maxHp += GAME_CONSTANTS.LEVEL_UP_HP;
      this.attack += GAME_CONSTANTS.LEVEL_UP_ATK;
      this.hp = this.maxHp;
      leveledUp = true;
    }
    return leveledUp;
  }

  isDead() {
    return this.hp <= 0;
  }
}

// モンスタークラス
class Monster {
  constructor(data) {
    this.name = data.name;
    this.hp = data.hp;
    this.maxHp = data.hp;
    this.attack = data.attack;
    this.exp = data.exp;
    this.image = data.image;
    this.rate = data.rate;
  }

  takeDamage(damage) {
    this.hp = Math.max(0, this.hp - damage);
    return this.hp <= 0;
  }

  isDead() {
    return this.hp <= 0;
  }

  clone() {
    return new Monster({
      name: this.name,
      hp: this.maxHp,
      attack: this.attack,
      exp: this.exp,
      image: this.image,
      rate: this.rate
    });
  }
}

// モンスターファクトリークラス
class MonsterFactory {
  static MONSTERS = [
    {
      name: "スライム",
      hp: 20,
      attack: 10,
      exp: 10,
      image: "image/Slime.webp",
      rate: 0.65
    },
    {
      name: "ドラゴン",
      hp: 40,
      attack: 20,
      exp: 15,
      image: "image/dragon.webp",
      rate: 0.25
    },
    {
      name: "メタルスライム",
      hp: 20,
      attack: 10,
      exp: 30,
      image: "image/Metal_slime.webp",
      rate: 0.1
    }
  ];

  static chooseMonster() {
    const random = Math.random();
    let cumulativeRate = 0;
    
    for (const monsterData of this.MONSTERS) {
      cumulativeRate += monsterData.rate;
      if (random <= cumulativeRate) {
        return new Monster(monsterData);
      }
    }
    
    return new Monster(this.MONSTERS[this.MONSTERS.length - 1]);
  }
}

// 戦闘クラス
class Battle {
  constructor(hero, monster, logManager, uiManager) {
    this.hero = hero;
    this.monster = monster;
    this.logManager = logManager;
    this.uiManager = uiManager;
  }

  start() {
    this.logManager.addLog(`${this.monster.name}と戦闘が始まりました！`);
    this.uiManager.updateCharacterImage(this.monster.image);
    this.uiManager.toggleControls(false);
  }

  heroAttack() {
    if (!this.monster) return;

    this.monster.takeDamage(this.hero.attack);
    this.logManager.addLog(`ヒーローの攻撃！ ${this.monster.name} に${this.hero.attack}のダメージ！`);

    if (this.monster.isDead()) {
      this.handleMonsterDefeat();
    } else {
      this.monsterAttack();
    }
  }

  monsterAttack() {
    const isDead = this.hero.takeDamage(this.monster.attack);
    
    if (isDead) {
      this.logManager.addLog(`${this.monster.name} の攻撃！ ヒーローは倒れました...`);
      this.uiManager.showGameOverPopup();
    } else {
      this.logManager.addLog(`${this.monster.name} の攻撃！ ヒーローは${this.monster.attack}のダメージを受けた！`);
    }
  }

  handleMonsterDefeat() {
    this.logManager.addLog(`${this.monster.name} を倒した！ 経験値: ${this.monster.exp} 獲得！`);
    
    const leveledUp = this.hero.gainExp(this.monster.exp);
    if (leveledUp) {
      this.logManager.addLog(`レベルアップ！ レベル: ${this.hero.level}、HP: ${this.hero.hp}、攻撃力: ${this.hero.attack}`);
    }
    
    this.end();
  }

  tryEscape() {
    const escapeChance = Math.random();
    if (escapeChance < GAME_CONSTANTS.ESCAPING_RATE) {
      this.logManager.addLog("うまく逃げ切れました！");
      this.end();
    } else {
      this.logManager.addLog("逃げられませんでした！");
      this.monsterAttack();
    }
  }

  end() {
    this.uiManager.toggleControls(true);
    this.uiManager.updateCharacterImage(this.hero.image);
    this.monster = null;
  }
}

// UI管理クラス
class UIManager {
  constructor(logManager) {
    this.logManager = logManager;
    this.hpElement = document.getElementById("hp");
    this.attackElement = document.getElementById("attack");
    this.levelElement = document.getElementById("level");
    this.characterImage = document.getElementById("character-image");
    this.directionElement = document.getElementById("direction");
    this.choiceElement = document.getElementById("choice");
  }

  updateStatus(hero) {
    this.hpElement.textContent = hero.hp;
    this.attackElement.textContent = hero.attack;
    this.levelElement.textContent = hero.level;
  }

  updateCharacterImage(imagePath) {
    this.characterImage.src = imagePath;
  }

  toggleControls(isDirectionActive) {
    if (isDirectionActive) {
      this.directionElement.style.display = "flex";
      this.choiceElement.style.display = "none";
    } else {
      this.directionElement.style.display = "none";
      this.choiceElement.style.display = "flex";
    }
  }

  showGameOverPopup() {
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
      document.body.removeChild(popup);
      window.game.reset();
      this.logManager.addLog("主人公が蘇生しました。");
    });

    popup.appendChild(yesButton);
    document.body.appendChild(popup);
  }
}

// 地図管理クラス
class MapManager {
  constructor(logManager) {
    this.logManager = logManager;
    this.map = null;
    this.currentPosition = null;
    this.positionMarker = null;
    this.currentCircle = null;
  }

  initMap() {
    const tokyoStationPosition = [35.6812, 139.7671];
    
    this.map = L.map('map', {
      zoomControl: false,
      scrollWheelZoom: false,
      doubleClickZoom: false,
      boxZoom: false,
      keyboard: false,
      dragging: false,
      touchZoom: false,
      bounceAtZoomLimits: false
    }).setView(tokyoStationPosition, 13);

    this.addTileLayer();
    this.setupInitialMarkers(tokyoStationPosition);
    this.getCurrentPosition();
  }

  addTileLayer() {
    const tileProviders = [
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      'https://a.tile.openstreetmap.org/{z}/{x}/{y}.png',
      'https://b.tile.openstreetmap.org/{z}/{x}/{y}.png',
      'https://c.tile.openstreetmap.org/{z}/{x}/{y}.png'
    ];

    let tileLayerAdded = false;
    
    for (let i = 0; i < tileProviders.length && !tileLayerAdded; i++) {
      try {
        const tileLayer = L.tileLayer(tileProviders[i], {
          attribution: '© OpenStreetMap contributors',
          maxZoom: 19,
          timeout: 5000
        });
        tileLayer.addTo(this.map);
        tileLayerAdded = true;
      } catch (error) {
        // エラーを無視して次のプロバイダーを試行
      }
    }

    if (!tileLayerAdded) {
      document.getElementById('map').style.backgroundColor = '#e0e0e0';
      document.getElementById('map').innerHTML = '<div style="text-align: center; padding-top: 50px; color: #666;">地図を読み込めませんでした<br>東京駅周辺の地図</div>';
    }
  }

  setupInitialMarkers(position) {
    const marker = L.marker(position, { title: "東京駅" }).addTo(this.map);
    const circle = L.circle(position, {
      color: 'red',
      fillColor: '#f03',
      fillOpacity: 0.1,
      radius: 500
    }).addTo(this.map);
  }

  getCurrentPosition() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => this.handlePositionSuccess(position),
        (error) => this.handlePositionError(error),
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 60000
        }
      );
    } else {
      this.logManager.addLog("このブラウザは位置情報をサポートしていません。東京駅周辺の地図を表示しています。");
    }
  }

  handlePositionSuccess(position) {
    this.currentPosition = [position.coords.latitude, position.coords.longitude];
    
    // 既存のマーカーを削除
    this.map.eachLayer((layer) => {
      if (layer instanceof L.Marker || layer instanceof L.Circle) {
        this.map.removeLayer(layer);
      }
    });
    
    this.map.setView(this.currentPosition, 13);
    
    this.positionMarker = L.marker(this.currentPosition, {
      title: "現在位置"
    }).addTo(this.map);

    this.currentCircle = L.circle(this.currentPosition, {
      color: 'red',
      fillColor: '#f03',
      fillOpacity: 0.1,
      radius: 500
    }).addTo(this.map);

    this.logManager.addLog("現在位置を取得しました。");
  }

  handlePositionError(error) {
    this.logManager.addLog("位置情報の取得に失敗しました。東京駅を中心とした地図を表示しています。");
    
    const errorMessages = {
      [error.PERMISSION_DENIED]: "位置情報の許可が拒否されました。",
      [error.POSITION_UNAVAILABLE]: "位置情報が利用できません。",
      [error.TIMEOUT]: "位置情報の取得がタイムアウトしました。"
    };
    
    const message = errorMessages[error.code] || "位置情報の取得でエラーが発生しました。";
    this.logManager.addLog(message + "東京駅周辺の地図を表示しています。");
  }

  movePosition(direction) {
    if (!this.currentPosition || !this.positionMarker || !this.currentCircle) return;

    const newPosition = this.calculateNewPosition(this.currentPosition, direction);
    this.currentPosition = newPosition;
    
    this.positionMarker.setLatLng(newPosition);
    this.currentCircle.setLatLng(newPosition);
    this.map.setView(newPosition, 13);
  }

  calculateNewPosition(currentPos, direction) {
    const latDiff = 0.001;
    const lngDiff = 0.001;
    
    switch(direction) {
      case "上": return [currentPos[0] + latDiff, currentPos[1]];
      case "下": return [currentPos[0] - latDiff, currentPos[1]];
      case "左": return [currentPos[0], currentPos[1] - lngDiff];
      case "右": return [currentPos[0], currentPos[1] + lngDiff];
      default: return currentPos;
    }
  }
}

// ゲーム管理クラス
class Game {
  constructor() {
    this.logManager = new LogManager();
    this.uiManager = new UIManager(this.logManager);
    this.mapManager = new MapManager(this.logManager);
    this.hero = new Hero();
    this.currentMonster = null;
    this.currentBattle = null;
  }

  init() {
    this.reset();
    this.mapManager.initMap();
    this.setupEventListeners();
  }

  reset() {
    this.hero.reset();
    this.currentMonster = null;
    this.currentBattle = null;
    this.uiManager.updateCharacterImage(this.hero.image);
    this.uiManager.toggleControls(true);
    this.uiManager.updateStatus(this.hero);
  }

  async encounterMonster() {
    try {
      const res = await fetch('/api/encounter');
      const data = await res.json();
      
      if (data.appear) {
        this.currentMonster = new Monster(data.monster);
        this.logManager.addLog(`${this.currentMonster.name} が現れた！`);
        this.uiManager.updateCharacterImage(this.currentMonster.image);
        this.uiManager.toggleControls(false);
        
        this.currentBattle = new Battle(this.hero, this.currentMonster, this.logManager, this.uiManager);
        this.currentBattle.start();
      } else {
        this.logManager.addLog("何も見つかりませんでした。");
        this.uiManager.toggleControls(true);
        this.uiManager.updateCharacterImage(this.hero.image);
      }
    } catch (err) {
      this.logManager.addLog(`出現判定エラー: ${err}`);
    }
  }

  moveHero(direction) {
    this.logManager.addLog(`${direction}方向に進みました。`);
    this.mapManager.movePosition(direction);
    this.encounterMonster();
  }

  attackMonster() {
    if (this.currentBattle) {
      this.currentBattle.heroAttack();
      this.uiManager.updateStatus(this.hero);
    }
  }

  tryToEscape() {
    if (this.currentBattle) {
      this.currentBattle.tryEscape();
      this.uiManager.updateStatus(this.hero);
    }
  }

  setupEventListeners() {
    document.getElementById("up").addEventListener("click", () => this.moveHero("上"));
    document.getElementById("down").addEventListener("click", () => this.moveHero("下"));
    document.getElementById("left").addEventListener("click", () => this.moveHero("左"));
    document.getElementById("right").addEventListener("click", () => this.moveHero("右"));
    document.getElementById("Start-fighting").addEventListener("click", () => this.attackMonster());
    document.getElementById("run-away").addEventListener("click", () => this.tryToEscape());
  }
}

// グローバルゲームインスタンス
window.game = null;

// 初期化
window.onload = () => {
  window.game = new Game();
  window.game.init();
};
