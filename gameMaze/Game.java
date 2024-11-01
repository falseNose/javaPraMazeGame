package gameMaze;
import com.sun.source.tree.IfTree;
import org.w3c.dom.html.HTMLBRElement;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;

class GameObject {
    protected int x, y; // 객체의 x, y 좌표
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x; // x 좌표 반환
    }
    public int getY() {
        return y; // y 좌표 반환
    }
    public void move(int dx, int dy) {
        x += dx; // x 좌표 이동
        y += dy; // y 좌표 이동
    }
}
class Player extends GameObject {
    int score = 0,armor = 0,potion = 0,map = 0,health = 100,key = 0,shield = 0;

    public Player(int x, int y) {
        super(x, y); // GameObject의 생성자 호출
    }
    public void useKey() {
        key--; // 열쇠 사용
        System.out.println("Key를 사용하였습니다.");
    }
    public void useMap() {
        map--; // 지도 사용
        System.out.println("Map을 사용하였습니다.");
    }
    public void useShield() {
        shield--; // 방패 사용
        armor += 40;
        System.out.println("Shield를 사용하였습니다\n현제 아머" + armor);
    }
    public void usePotion() {
        potion--; // 포션 사용
        health += 35;
        System.out.println("포션을 사용하였습니다\n현제 채력 : " + health);
    }
    public void showItems() {
        Scanner scanner = new Scanner(System.in); // 사용자 입력을 위한 Scanner 객체 생성
        System.out.println("현재 아이템 목록:\n" + "열쇠: " + key + "\n방패: " + shield + "\n포션: " + potion);
        System.out.println("사용할 아이템을 선택하세요:\n" + "1: 포션 사용\n" + "2: 방패 사용\n" + "4: 돌아가기");
        int choice = scanner.nextInt(); // 아이템 선택 입력 받기
        switch (choice) {
            case 1:
                usePotion(); // 포션 사용
                break;
            case 2:
                useShield(); // 방패 사용
                break;
            case 3:
                if (map > 0) {
                    useMap(); // 지도 사용
                } else {
                    System.out.println("사용할 지도가 없습니다."); // 지도가 없는 경우
                }
                break;
            case 4:
                System.out.println("아이템 목록을 종료합니다."); // 목록 종료 메시지
                break;
            default:
                System.out.println("잘못된 선택입니다."); // 잘못된 입력 처리
        }
    }
}
class Trap {
    private int damage = 20; // 함정의 피해량

    public void caughtTrap(Player player) {
        player.health -= damage; // 함정에 걸렸을 때 체력 감소
        System.out.println("함정에 걸렸습니다. 체력이 " + damage + " 감소했습니다.");
    }
}
class Maze {
    private char[][] map;
    // 미로 배열 - 난이도 별로 세 가지 버전 준비
    private final char[][] MapLevel1 = {
            {'S', ' ', 'T', '#', ' ', ' ', '#', ' ', ' ', 'E'},
            {'#', '#', ' ', '#', ' ', '#', '#', '#', ' ', '#'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' '},
            {' ', '#', '#', '#', '#', ' ', '#', '#', ' ', '#'},
            {' ', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' '},
            {'#', '#', '#', ' ', '#', '#', '#', '#', '#', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' '},
            {' ', '#', '#', '#', '#', '#', '#', ' ', '#', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', '#', ' ', ' ', ' '},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', 'E'}
    };
    private final char[][] mapLevel2 = {
            {'S', ' ', 'K', '#', 'K', ' ', '#', 'S', ' ', '#'},
            {'#', '#', ' ', '#', '#', 'T', '#', '#', 'D', '#'},
            {'#', ' ', 'P', ' ', ' ', ' ', ' ', '#', ' ', '#'},
            {'#', '#', '#', '#', '#', ' ', '#', '#', ' ', ' '},
            {' ', ' ', ' ', 'T', ' ', ' ', ' ', '#', ' ', '#'},
            {'#', '#', '#', '#', '#', 'T', ' ', ' ', ' ', '#'},
            {' ', ' ', ' ', ' ', ' ', ' ', '#', '#', '#', '#'},
            {' ', '#', '#', 'T', ' ', '#', ' ', ' ', ' ', '#'},
            {'D', 'T', ' ', ' ', '#', '#', '#', '#', '#', '#'},
            {'P', '#', '#', ' ', ' ', ' ', ' ', ' ', ' ', 'E'}
    };
    private final char[][] mapLevel3 = {
            {'S', '#', '#', 'S', '#', ' ', ' ', '#', 'K', 'K'},
            {' ', '#', 'P', '#', '#', 'P', 'D', '#', ' ', '#'},
            {'K', '#', ' ', ' ', '#', '#', ' ', '#', 'S', '#'},
            {' ', ' ', 'D', 'T', '#', ' ', ' ', ' ', 'T', '#'},
            {'#', '#', '#', ' ', '#', ' ', '#', '#', '#', '#'},
            {'#', 'T', '#', ' ', 'T', ' ', '#', ' ', 'P', '#'},
            {'P', ' ', '#', '#', '#', ' ', ' ', 'D', ' ', '#'},
            {'#', ' ', ' ', ' ', 'T', ' ', '#', '#', '#', '#'},
            {'#', ' ', '#', '#', '#', '#', 'T', 'T', ' ', ' '},
            {'#', ' ', 'T', 'T', 'T', 'T', 'T', '#', '#', 'E'}
    };
    private final char[][] mapLevel4 = {
            {'S', '#', '#', '#', '#', '#', ' ', ' ', '#', '#', 'K', '#', '#', '#', '#'},
            {' ', '#', 'P', '#', '#', 'P', 'D', '#', ' ', ' ', ' ', '#', '#', '#', '#'},
            {'K', '#', ' ', ' ', '#', '#', ' ', '#', 'S', '#', ' ', ' ', '#', ' ', '#'},
            {' ', ' ', 'D', 'T', '#', ' ', ' ', ' ', 'T', '#', ' ', ' ', ' ', ' ', '#'},
            {'#', '#', '#', ' ', '#', ' ', '#', '#', '#', '#', '#', ' ', '#', '#', '#'},
            {'#', 'T', '#', ' ', 'T', ' ', '#', ' ', 'P', '#', ' ', ' ', ' ', ' ', ' '},
            {'P', ' ', '#', '#', '#', ' ', ' ', 'D', ' ', '#', '#', '#', ' ', '#', ' '},
            {'#', ' ', ' ', ' ', 'T', ' ', '#', '#', '#', '#', ' ', ' ', ' ', 'T', '#'},
            {'#', ' ', '#', '#', '#', '#', 'T', 'T', ' ', ' ', ' ', '#', '#', ' ', ' '},
            {'#', ' ', 'T', 'T', 'T', 'T', 'T', '#', '#', '#', ' ', '#', '#', '#', '#'},
            {'#', ' ', '#', '#', ' ', ' ', '#', '#', '#', '#', ' ', ' ', ' ', '#', '#'},
            {'#', ' ', ' ', '#', ' ', ' ', ' ', ' ', '#', '#', '#', ' ', '#', ' ', ' '},
            {'#', '#', ' ', ' ', '#', '#', 'P', ' ', ' ', ' ', ' ', 'T', ' ', '#', '#'},
            {'#', '#', 'K', 'D', 'P', 'S', '#', '#', '#', ' ', '#', 'T', 'T', 'T', ' '},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', 'E'},
    };

    private final char[][] mapLevel0 = {
            {'S', '#', '#', 'S', '#', '#', ' ', '#', ' ', 'E'},
            {' ', '#', 'P', ' ', ' ', '#', 'D', '#', ' ', '#'},
            {' ', '#', ' ', '#', ' ', '#', ' ', '#', 'T', '#'},
            {' ', 'K', 'D', '#', ' ', 'K', ' ', 'S', 'T', '#'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'P', 'P', 'P', 'P', 'P', 'p', 'P', 'P', 'P', 'P'},
            {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
            {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}
    };
    public Maze(int level) {
        setDifficulty(level);
    }
    public void setDifficulty(int level) {
        switch (level) {
            case 1:
                map = MapLevel1;
                System.out.println("level 1 게임 시작");
                break;
            case 2:
                map = mapLevel2;
                System.out.println("level 2 게임 시작");
                break;
            case 3:
                map = mapLevel3;
                System.out.println("level 3 게임 시작");
                break;
            case 4:
                map = mapLevel4;
                System.out.println("level 3 게임 시작");
                break;
            case 0:
                map = mapLevel0;
                System.out.println("튜토리얼 시작.");
                break;
            default:
                System.out.println("잘못된 난이도입니다. 기본 난이도(1)로 설정됩니다.");
                map = MapLevel1;
        }
    }
    public char[][] getMap() {
        return map; // 미로 배열 반환
    }
    public boolean isExit(int x, int y) {
        return map[y][x] == 'E'; // Exit인지 확인
    }
    public boolean isWall(int x, int y) {
        return map[y][x] == '#'; // 벽인지 확인
    }
    public boolean isDoor(int x, int y) {
        return map[y][x] == 'D'; // 문인지 확인
    }
    public boolean isKey(int x, int y) {
        return map[y][x] == 'K'; // 키인지 확인
    }
    public boolean isTrap(int x, int y) {
        return map[y][x] == 'T'; // 함정인지 확인
    }
    public boolean isShield(int x, int y) {
        return map[y][x] == 'S'; //쉴드 인지 확인
    }
    public boolean isPotion(int x, int y) {
        return map[y][x] == 'P'; //포션 인지 확인
    }
    public void openDoor(int x, int y) {
        map[y][x] = ' '; // 문열림 확인
    }
    public void getPotion(int x, int y) {
        if (isPotion(x, y)) {
            map[y][x] = ' '; // 포션 획득 후 맵에서 제거
        }
    }
    public void getShield(int x, int y) {
        if (isShield(x, y)) {
            map[y][x] = ' '; // 쉴드 획득 후 맵에서 제거
        }
    }
    public void getKey(int x, int y) {
        if (isKey(x, y)) {
            map[y][x] = ' '; // 키 획득 후 맵에서 제거
        }
    }
    public static class Game {
        private final Maze maze;
        private final Player player;
        private Timer timer;
        private int remainingTime = 300;

        public Game(int level) {
            this.maze = new Maze(level);
            this.player = new Player(0, 0); // 시작 지점
        }

        private void startTimer() {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (remainingTime > 0) {
                        remainingTime--;
                    } else {
                        System.out.println("시간이 다 되었습니다! 게임 오버!");
                        System.exit(0); // 게임 종료
                    }
                }
            }, 1000, 1000); // 1초마다 실행
        }

        // 문을 통과할 수 있는지 확인하는 메서드
        private boolean meetDoor(Player player, int x, int y) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("문이 있습니다. 열쇠를 사용하시겠습니까?\n (사용한다 : 1)\n (사용하지 않는다 : 2)");
            int choice = scanner.nextInt();
            if (choice == 1) {
                if (player.key > 0) {
                    player.useKey(); // 열쇠 사용
                    System.out.println("열쇠를 사용하여 문을 열었습니다. 남은 열쇠 수: " + player.key);
                    maze.openDoor(x, y); // 문 제거
                    return true;
                } else {
                    System.out.println("열쇠가 없습니다!"); // 열쇠가 없는 경우
                }
            } else {
                System.out.println("문을 열지 않았습니다."); // 문을 열지 않는 경우
            }
            return false;
        }

        public void play() {
            startTimer();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                printMaze(); // 현재 미로 상태 출력
                System.out.println("이동할 방향을 입력하세요 (WASD):\n" + "아이템을 사용 하시려면 'I'를 누르세요");
                System.out.println("남은 시간: " + remainingTime + "초");
                char direction = scanner.next().toUpperCase().charAt(0); // 방향 입력 받기
                int dx = 0, dy = 0;

                switch (direction) {
                    case 'W':
                        dy = -1; // 위로 이동
                        break;
                    case 'S':
                        dy = 1; // 아래로 이동
                        break;
                    case 'A':
                        dx = -1; // 왼쪽으로 이동
                        break;
                    case 'D':
                        dx = 1; // 오른쪽으로 이동
                        break;
                    case 'I':
                        player.showItems();

                        break;
                    default:
                        System.out.println("잘못된 입력입니다."); // 잘못된 입력 처리
                        continue; // 잘못된 입력이면 다음 반복으로 넘어감
                }
                // 새로운 위치 계산
                int newX = player.getX() + dx;
                int newY = player.getY() + dy;

                if (newX >= 0 && newY >= 0 && newY < maze.getMap().length && newX < maze.getMap()[0].length
                        && !maze.isWall(newX, newY)) {
                    // 문인 경우 열쇠 사용 확인
                    if (maze.isDoor(newX, newY)) {
                        if (!meetDoor(player, newX, newY)) { // meetDoor가 false면 이동하지 않음
                            continue; // 문을 통과하지 못하면 다음 반복으로 넘어감
                        }
                    }
                    player.move(dx, dy); // 플레이어 이동
                } else {
                    System.out.println("벽에 막혔습니다!"); // 벽에 부딪힌 경우
                }
                // 플레이어가 exit에 도착했는지 확인
                if (maze.isExit(player.getX(), player.getY())) {
                    System.out.println("축하합니다! 미로를 탈출했습니다!\n" + "player의 점수는 : " + remainingTime); // 게임 승리 메시지
                    player.score += remainingTime;
                    System.exit(0); // 게임 종료
                }
                // 아이템 획득 확인
                if (maze.isKey(player.getX(), player.getY())) {
                    player.key++; // 키 수 증가
                    System.out.println("키 아이템을 획득 했습니다.");
                    maze.getKey(player.getX(), player.getY());
                }
                if (maze.isShield(player.getX(), player.getY())) {
                    player.shield++; // 쉴드 수 증가
                    System.out.println("쉴드 아이템을 획득 했습니다.");
                    maze.getShield(player.getX(), player.getY());
                }
                if (maze.isPotion(player.getX(), player.getY())) {
                    player.potion++; // 포션 수 증가
                    System.out.println("포션 아이템을 획득 했습니다.");
                    maze.getPotion(player.getX(), player.getY());
                }
                if (maze.isTrap(player.getX(), player.getY())) {
                    System.out.println("함정에 걸렸습니다."); // 함정에 걸린 경우
                    if (player.armor > 0) {
                        player.armor -= 40;
                    } else {
                        player.health -= 20; // 체력 감소
                    }
                    System.out.println("현재 체력: " + player.health + "현제 아머" + player.armor);
                    if (player.health <= 0) {
                        System.out.println("게임 오버!"); // 체력이 0 이하가 되면 게임 오버
                        System.exit(0); // 게임 종료
                    }
                }
            }
        }
        private void printMaze() {
            char[][] map = maze.getMap();
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (player.getX() == x && player.getY() == y) {
                        System.out.print("★ "); // 플레이어 위치 표시
                    } else {
                        System.out.print(map[y][x] + " "); // 미로의 각 요소 출력
                    }
                }
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆\n'S'start point 부터 시작해 'E' exit 으로 나가면 승리하는 게임입니다.\n'#'벽을 피해 'W' 앞 'A' 왼쪽 'S' 뒤 'D' 오른쪽 으로 조작해 탈출 하시면됩니다 \n언제든지 'I'를 눌러 item 을 확인 하실 수 있습니다" +
                "\n플레이어 '★'의 기본 health은 100으로 시작합니다\n" +
                "trap'T'를 밟으면 채력이 20 감소 합니다.\npotion 'P'를 얻으면 아이템창에서 사용 가능합니다 채력이 35 증가합니다\n'D' door에 가로 막히면 'K' key를 통해 통과 하실 수 있습니다\n" +
                "'S' shield를 사용하면 armor가 40 증가합니다.\narmor는 health 보다 우선적으로 감소하지만 2배의 감소수치를 갖습니다.");
        System.out.println("\n시작할 게임의 난이도를 고르세요(level 1 ~ level 4): \n튜토리얼을 하시려면 0번을 입력하세요\n☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆\n");
        int level = scanner.nextInt(); // 난이도를 입력받아 저장
        System.out.println();
        Game game = new Game(level); // 선택한 난이도로 게임 시작
        game.play();
    }
}

