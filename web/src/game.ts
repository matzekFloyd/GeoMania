import p5 from "p5";

type Shape = "square" | "triangle" | "circle";

type Vec = { x: number; y: number };

class BasicStuff {
  x: number;
  y: number;
  width: number;
  height: number;
  speed: Vec;
  acceleration: Vec;
  dead: boolean;

  constructor(x: number, y: number, width = 0, height = 0) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.speed = { x: 0, y: 0 };
    this.acceleration = { x: 0, y: 0 };
    this.dead = false;
  }

  touches(other: BasicStuff): boolean {
    return (
      this.x < other.x + other.width &&
      this.x + this.width > other.x &&
      this.y < other.y + other.height &&
      this.y + this.height > other.y
    );
  }
}

class Player extends BasicStuff {
  timeAtLastShot = 0;

  constructor(x: number, y: number) {
    super(x, y, 30, 30);
  }

  updatePosition(w: number, h: number): void {
    this.speed.x += this.acceleration.x;
    this.speed.y += this.acceleration.y;
    this.acceleration.x *= 0.5;
    this.acceleration.y *= 0.5;
    this.speed.x *= 0.9;
    this.speed.y *= 0.9;

    const speedMagnitude = Math.hypot(this.speed.x, this.speed.y);
    if (speedMagnitude >= 10) {
      this.speed.x = (this.speed.x / speedMagnitude) * 10;
      this.speed.y = (this.speed.y / speedMagnitude) * 10;
    }

    if (this.x + this.width + this.speed.x > w || this.x + this.speed.x < 0) {
      this.speed.x *= -1;
    }
    if (this.y + this.height + this.speed.y > h || this.y + this.speed.y < 0) {
      this.speed.y *= -1;
    }

    this.x += this.speed.x;
    this.y += this.speed.y;
  }
}

class Enemy extends BasicStuff {
  level: number;
  shape: Shape;

  constructor(shape: Shape, x: number, y: number, speed: Vec, level: number) {
    super(x, y, 50, 50);
    this.shape = shape;
    this.speed = speed;
    this.level = level;
  }

  updatePosition(w: number, h: number): void {
    this.width = 20 + this.level * 10;
    this.height = 20 + this.level * 10;

    if (this.x + this.width + this.speed.x > w || this.x + this.speed.x < 0) {
      this.speed.x *= -1;
    }
    if (this.y + this.height + this.speed.y > h || this.y + this.speed.y < 0) {
      this.speed.y *= -1;
    }

    const speedMagnitude = Math.hypot(this.speed.x, this.speed.y);
    if (speedMagnitude >= 10) {
      this.speed.x = (this.speed.x / speedMagnitude) * 10;
      this.speed.y = (this.speed.y / speedMagnitude) * 10;
    }

    this.x += this.speed.x;
    this.y += this.speed.y;
  }
}

class Shot extends BasicStuff {
  beginTime: number;
  decider: number;

  constructor(x: number, y: number, speed: Vec, nowSeconds: number, decider: number) {
    super(x, y, 10, 10);
    this.speed = speed;
    this.beginTime = nowSeconds;
    this.decider = decider;
  }

  updatePosition(nowSeconds: number): void {
    if (nowSeconds - this.beginTime > 3) {
      this.dead = true;
    }

    const speedMagnitude = Math.hypot(this.speed.x, this.speed.y);
    if (speedMagnitude >= 10) {
      this.speed.x = (this.speed.x / speedMagnitude) * 10;
      this.speed.y = (this.speed.y / speedMagnitude) * 10;
    }

    this.x += this.speed.x;
    this.y += this.speed.y;
  }
}

const enemyRandomizer = () => 0.5 + Math.random();
const points = (level: number) => [125, 100, 75, 50, 25][level] ?? 0;
const clamp = (value: number, min: number, max: number) => Math.max(min, Math.min(max, value));
const WORLD_SIZE = 800;
const MIN_CANVAS_SCALE = 0.5;
const MAX_CANVAS_SCALE = 1.75;
const BACKGROUND_IMAGE_URL = new URL("geometry.jpg", document.baseURI).href;

/** Inset from canvas edges: D-pad bottom/right and HUD bottom/left stay aligned. */
const EDGE_MARGIN = 24;

/** On-screen D-pad in sketch coordinates (800×800). Bottom-right cross layout. */
const PAD = (() => {
  const btn = 52;
  const gap = 8;
  const cx = WORLD_SIZE - EDGE_MARGIN - gap - btn;
  const cy = WORLD_SIZE - EDGE_MARGIN - gap - btn;
  const up = { x: cx - btn / 2, y: cy - btn - gap, w: btn, h: btn };
  const down = { x: cx - btn / 2, y: cy + gap, w: btn, h: btn };
  const left = { x: cx - btn - gap, y: cy - btn / 2, w: btn, h: btn };
  const right = { x: cx + gap, y: cy - btn / 2, w: btn, h: btn };
  return { btn, gap, cx, cy, up, down, left, right };
})();

const HUD_BOTTOM_BASELINE = WORLD_SIZE - EDGE_MARGIN;
const HUD_LEVEL_BASELINE = HUD_BOTTOM_BASELINE - 22;
const HUD_LEFT = EDGE_MARGIN;

const pointInRect = (px: number, py: number, rx: number, ry: number, rw: number, rh: number) =>
  px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;

const readPadFromPoint = (
  px: number,
  py: number,
  out: { up: boolean; down: boolean; left: boolean; right: boolean }
) => {
  if (pointInRect(px, py, PAD.up.x, PAD.up.y, PAD.up.w, PAD.up.h)) out.up = true;
  if (pointInRect(px, py, PAD.down.x, PAD.down.y, PAD.down.w, PAD.down.h)) out.down = true;
  if (pointInRect(px, py, PAD.left.x, PAD.left.y, PAD.left.w, PAD.left.h)) out.left = true;
  if (pointInRect(px, py, PAD.right.x, PAD.right.y, PAD.right.w, PAD.right.h)) out.right = true;
};

const readOnScreenPad = (p: p5) => {
  const pad = { up: false, down: false, left: false, right: false };
  for (let i = 0; i < p.touches.length; i += 1) {
    const t = p.touches[i] as { x: number; y: number };
    readPadFromPoint(t.x, t.y, pad);
  }
  if (p.mouseIsPressed && p.mouseButton === p.LEFT) {
    readPadFromPoint(p.mouseX, p.mouseY, pad);
  }
  return pad;
};

const DEAD_TOUCH = {
  retry: { x: 150, y: 500, w: 500, h: 52 },
  menu: { x: 150, y: 565, w: 500, h: 52 },
};

const drawDirectionalPad = (p: p5, active: { up: boolean; down: boolean; left: boolean; right: boolean }) => {
  const drawBtn = (r: { x: number; y: number; w: number; h: number }, isOn: boolean, label: string) => {
    p.fill(isOn ? 80 : 40, isOn ? 80 : 40, isOn ? 80 : 40, 200);
    p.stroke(220);
    p.strokeWeight(2);
    p.rect(r.x, r.y, r.w, r.h, 8);
    p.noStroke();
    p.fill(255);
    p.textSize(22);
    p.textAlign(p.CENTER, p.CENTER);
    p.text(label, r.x + r.w / 2, r.y + r.h / 2 + 1);
    p.textAlign(p.LEFT, p.BASELINE);
  };
  drawBtn(PAD.up, active.up, "↑");
  drawBtn(PAD.down, active.down, "↓");
  drawBtn(PAD.left, active.left, "←");
  drawBtn(PAD.right, active.right, "→");
};

export const mountGeoMania = (mountElement: HTMLElement): void => {
  let gameStartTime = 0;
  let frozenTime = 0;
  let score = 0;
  let currentLevel = 0;
  let pause = false;

  const clearMovementKeys = () => {};

  let player = new Player(400, 400);
  let enemies: Enemy[] = [];
  let shots: Shot[] = [];
  let backgroundImage: p5.Image | null = null;

  const resetLevel = () => {
    player = new Player(400, 400);
    enemies = [];
    shots = [];

    if (currentLevel === 1) {
      enemies.push(new Enemy("square", 300, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 4));
    }
    if (currentLevel === 2) {
      enemies.push(new Enemy("square", 0, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("triangle", 400, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("circle", 750, 400, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
    }
    if (currentLevel === 3) {
      enemies.push(new Enemy("square", 0, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("square", 0, 375, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("square", 0, 750, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("triangle", 400, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("triangle", 400, 750, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("circle", 750, 0, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("circle", 750, 400, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
      enemies.push(new Enemy("circle", 750, 750, { x: enemyRandomizer(), y: enemyRandomizer() }, 2));
    }
  };

  const startGame = (p: p5) => {
    currentLevel = 1;
    gameStartTime = p.millis() / 1000;
    frozenTime = 0;
    score = 0;
    resetLevel();
  };

  const fullRestart = () => {
    currentLevel = 0;
    pause = false;
    score = 0;
    gameStartTime = 0;
    frozenTime = 0;
    clearMovementKeys();
    player = new Player(400, 400);
    enemies = [];
    shots = [];
  };

  const spawnShot = (p: p5, direction: Vec) => {
    const now = p.millis() / 1000;
    if (now - player.timeAtLastShot <= 0.5 || player.dead) {
      return;
    }
    player.timeAtLastShot = now;
    shots.push(new Shot(player.x + player.width / 2, player.y + player.height / 2, direction, now, p.random(0, 1.5)));
  };

  const splitEnemy = (enemy: Enemy) => {
    if (enemy.level === 0) {
      return;
    }

    const center = { x: 400, y: 400 };
    const d = { x: enemy.x - center.x, y: enemy.y - center.y };
    const dMag = Math.max(Math.hypot(d.x, d.y), 0.0001);
    const n = { x: d.x / dMag, y: d.y / dMag };
    const e1 = { x: -n.y, y: n.x };
    const e2 = { x: n.y, y: -n.x };
    const offset = 50;
    const speedMag = Math.max(Math.hypot(enemy.speed.x, enemy.speed.y), 0.5);
    const speed1 = { x: e1.x * speedMag, y: e1.y * speedMag };
    const speed2 = { x: e2.x * speedMag, y: e2.y * speedMag };

    const newX1 = clamp(enemy.x + offset * e1.x, 50, 750);
    const newY1 = clamp(enemy.y + offset * e1.y, 50, 750);
    const newX2 = clamp(enemy.x + offset * e2.x, 50, 750);
    const newY2 = clamp(enemy.y + offset * e2.y, 50, 750);

    enemies.push(new Enemy(enemy.shape, newX1, newY1, speed1, enemy.level - 1));
    enemies.push(new Enemy(enemy.shape, newX2, newY2, speed2, enemy.level - 1));
  };

  const sketch = (p: p5) => {
    let canvas: p5.Renderer;

    const fitCanvasToViewport = () => {
      const styles = window.getComputedStyle(mountElement);
      const horizontalPadding = parseFloat(styles.paddingLeft) + parseFloat(styles.paddingRight);
      const verticalPadding = parseFloat(styles.paddingTop) + parseFloat(styles.paddingBottom);
      const containerWidth = mountElement.clientWidth - horizontalPadding;
      const containerHeight = mountElement.clientHeight - verticalPadding;
      const availableWidth = Math.max(containerWidth || window.innerWidth, 240);
      const availableHeight = Math.max(containerHeight || window.innerHeight, 240);
      const viewportScale = Math.min(availableWidth / WORLD_SIZE, availableHeight / WORLD_SIZE);
      const scale = clamp(viewportScale, MIN_CANVAS_SCALE, MAX_CANVAS_SCALE);
      canvas.style("width", `${WORLD_SIZE * scale}px`);
      canvas.style("height", `${WORLD_SIZE * scale}px`);
    };

    p.setup = () => {
      canvas = p.createCanvas(WORLD_SIZE, WORLD_SIZE);
      canvas.parent(mountElement);
      p.textFont("Arial");
      backgroundImage = p.loadImage(BACKGROUND_IMAGE_URL, undefined, () => {
        backgroundImage = null;
      });
      fitCanvasToViewport();
    };

    p.windowResized = () => {
      fitCanvasToViewport();
    };

    p.touchStarted = () => {
      if (currentLevel === 0) {
        startGame(p);
        return false;
      }
      if (player.dead && currentLevel > 0 && currentLevel < 4) {
        if (pointInRect(p.mouseX, p.mouseY, DEAD_TOUCH.retry.x, DEAD_TOUCH.retry.y, DEAD_TOUCH.retry.w, DEAD_TOUCH.retry.h)) {
          gameStartTime = p.millis() / 1000;
          score = 0;
          resetLevel();
        } else if (pointInRect(p.mouseX, p.mouseY, DEAD_TOUCH.menu.x, DEAD_TOUCH.menu.y, DEAD_TOUCH.menu.w, DEAD_TOUCH.menu.h)) {
          fullRestart();
        }
      }
      return false;
    };

    p.touchMoved = () => false;

    p.keyPressed = () => {
      const wasTitleScreen = currentLevel === 0;

      if (p.key === " ") {
        if (currentLevel > 0 && currentLevel < 4) {
          currentLevel += 1;
          if (currentLevel <= 3) {
            resetLevel();
          } else {
            frozenTime = Math.floor(p.millis() / 1000 - gameStartTime);
          }
        }
      }

      if (wasTitleScreen) {
        startGame(p);
      }

      if ((p.key === "t" || p.key === "T") && !wasTitleScreen && currentLevel > 0 && currentLevel < 4) {
        gameStartTime = p.millis() / 1000;
        score = 0;
        resetLevel();
      }

      if (p.key === "r" || p.key === "R") fullRestart();
      if (p.key === "p" || p.key === "P") pause = !pause;

      if (player.dead) {
        return;
      }
    };

    p.draw = () => {
      if (!p.focused) {
        clearMovementKeys();
      }

      if (pause) {
        return;
      }

      p.background(0);
      if (backgroundImage) p.image(backgroundImage, 0, 0, p.width, p.height);

      if (currentLevel === 0) {
        p.fill(255);
        p.textSize(25);
        p.text("Welcome to", 320, 300);
        p.textSize(50);
        p.text("Geomania", 275, 350);
        p.textSize(25);
        p.text("Press any key to start", 265, 500);
        p.textSize(12);
        p.text("© by Mathias 'Floyd' Mayrhofer", 600, 775);
        return;
      }

      if (currentLevel === 4) {
        p.fill(255);
        p.textSize(56);
        p.text("Game Over", 240, 350);
        p.textSize(42);
        p.text("You Won.", 280, 430);
        p.textSize(24);
        p.text(`Score: ${score}  Time: ${frozenTime}s`, 265, 490);
        p.textSize(18);
        p.text("Press R to restart", 315, 535);
        return;
      }

      if (enemies.length === 0) {
        currentLevel += 1;
        if (currentLevel === 4) {
          frozenTime = Math.floor(p.millis() / 1000 - gameStartTime);
        } else {
          resetLevel();
        }
      }

      const pad = readOnScreenPad(p);
      const upDown = p.keyIsDown(p.UP_ARROW) || p.keyIsDown(87) || pad.up;
      const downDown = p.keyIsDown(p.DOWN_ARROW) || p.keyIsDown(83) || pad.down;
      const rightDown = p.keyIsDown(p.RIGHT_ARROW) || p.keyIsDown(68) || pad.right;
      const leftDown = p.keyIsDown(p.LEFT_ARROW) || p.keyIsDown(65) || pad.left;

      const acceleration = { x: 0, y: 0 };
      if (upDown) {
        acceleration.y -= 0.5;
        spawnShot(p, { x: 0, y: -25 });
      }
      if (downDown) {
        acceleration.y += 0.5;
        spawnShot(p, { x: 0, y: 25 });
      }
      if (rightDown) {
        acceleration.x += 0.5;
        spawnShot(p, { x: 25, y: 0 });
      }
      if (leftDown) {
        acceleration.x -= 0.5;
        spawnShot(p, { x: -25, y: 0 });
      }

      player.acceleration = acceleration;
      player.updatePosition(800, 800);

      for (const enemy of enemies) {
        enemy.updatePosition(800, 800);
      }

      const now = p.millis() / 1000;
      for (const shot of shots) {
        shot.updatePosition(now);
      }

      for (let i = 0; i < enemies.length; i += 1) {
        const currentEnemy = enemies[i];
        if (!player.dead && currentEnemy.touches(player)) {
          player.dead = true;
        }

        for (let j = i + 1; j < enemies.length; j += 1) {
          const otherEnemy = enemies[j];
          if (currentEnemy.dead || otherEnemy.dead) continue;
          if (currentEnemy.shape !== otherEnemy.shape) continue;
          if (currentEnemy.level !== otherEnemy.level || currentEnemy.level >= 2) continue;
          if (!currentEnemy.touches(otherEnemy)) continue;

          currentEnemy.dead = true;
          otherEnemy.dead = true;
          enemies.push(new Enemy(currentEnemy.shape, otherEnemy.x, otherEnemy.y, otherEnemy.speed, otherEnemy.level + 1));
        }
      }

      for (const shot of shots) {
        if (shot.dead) continue;
        for (const enemy of enemies) {
          if (enemy.dead) continue;
          if (!shot.touches(enemy)) continue;
          shot.dead = true;
          enemy.dead = true;
          score += points(enemy.level);
          splitEnemy(enemy);
          break;
        }
      }

      enemies = enemies.filter((enemy) => !enemy.dead);
      shots = shots.filter((shot) => !shot.dead);

      for (const enemy of enemies) {
        if (enemy.shape === "square") {
          p.fill(255, 0, 0);
          p.rect(enemy.x, enemy.y, enemy.width, enemy.height);
        } else if (enemy.shape === "triangle") {
          p.fill(0, 0, 255);
          p.triangle(
            enemy.x + enemy.width / 2,
            enemy.y,
            enemy.x,
            enemy.y + enemy.height,
            enemy.x + enemy.width,
            enemy.y + enemy.height
          );
        } else {
          p.fill(0, 255, 0);
          p.ellipse(enemy.x + enemy.width / 2, enemy.y + enemy.height / 2, enemy.width, enemy.height);
        }
      }

      p.fill(255);
      for (const shot of shots) {
        if (shot.decider <= 0.5) {
          p.ellipse(shot.x, shot.y, shot.width, shot.height);
        } else if (shot.decider <= 1.0) {
          p.rect(shot.x, shot.y, shot.width, shot.height);
        } else {
          p.triangle(
            shot.x + shot.width / 2,
            shot.y,
            shot.x,
            shot.y + shot.height,
            shot.x + shot.width,
            shot.y + shot.height
          );
        }
      }

      p.fill(255);
      p.textSize(20);
      p.text(`Score : ${score}`, 50, 50);
      p.text(`Time : ${Math.floor(now - gameStartTime)}`, 650, 50);
      p.textSize(18);
      p.text(`Level ${currentLevel}`, HUD_LEFT, HUD_LEVEL_BASELINE);
      p.textSize(14);
      p.text("restart: 'r' = game, 't' = level", HUD_LEFT, HUD_BOTTOM_BASELINE);
      p.textSize(20);

      if (!player.dead) {
        p.fill(155);
        p.rect(player.x, player.y, player.width, player.height, 5);
        p.fill(200);
        p.ellipse(player.x + player.width / 2, player.y + player.height / 2, player.width, player.height);
      }

      if (player.dead) {
        p.fill(0, 0, 0, 170);
        p.rect(0, 0, 800, 800);
        p.fill(255);
        p.textSize(48);
        p.text("You Died", 300, 360);
        p.textSize(26);
        p.text("Press T to restart level", 265, 420);
        p.text("or R for full restart", 290, 460);
        p.fill(50, 120, 70, 230);
        p.rect(DEAD_TOUCH.retry.x, DEAD_TOUCH.retry.y, DEAD_TOUCH.retry.w, DEAD_TOUCH.retry.h, 10);
        p.fill(120, 60, 60, 230);
        p.rect(DEAD_TOUCH.menu.x, DEAD_TOUCH.menu.y, DEAD_TOUCH.menu.w, DEAD_TOUCH.menu.h, 10);
        p.fill(255);
        p.textSize(22);
        p.textAlign(p.CENTER, p.CENTER);
        p.text("Tap: restart level", DEAD_TOUCH.retry.x + DEAD_TOUCH.retry.w / 2, DEAD_TOUCH.retry.y + DEAD_TOUCH.retry.h / 2);
        p.text("Tap: back to title", DEAD_TOUCH.menu.x + DEAD_TOUCH.menu.w / 2, DEAD_TOUCH.menu.y + DEAD_TOUCH.menu.h / 2);
        p.textAlign(p.LEFT, p.BASELINE);
      } else {
        drawDirectionalPad(p, pad);
      }
    };
  };

  const instance = new p5(sketch);

  const handleBlur = () => clearMovementKeys();
  const handleVisibilityChange = () => {
    if (document.hidden) {
      clearMovementKeys();
    }
  };
  window.addEventListener("blur", handleBlur);
  document.addEventListener("visibilitychange", handleVisibilityChange);

  const cleanup = () => {
    window.removeEventListener("blur", handleBlur);
    document.removeEventListener("visibilitychange", handleVisibilityChange);
    instance.remove();
  };
  window.addEventListener("beforeunload", cleanup, { once: true });
};
