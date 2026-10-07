import os
import subprocess
import shutil
from py4j.java_gateway import JavaGateway, GatewayParameters, launch_gateway
import pygame
import threading


# =========================
# Java setup
# =========================

BASE = os.path.dirname(os.path.abspath(__file__))

JAVA_SRC = os.path.join(BASE, "java", "src")
JAVA_OUT = os.path.join(BASE, "java", "deploy")
PY4J_JAR = os.path.join(BASE, "lib", "py4j.jar")


# Delete the entire deploy folder if it exists
if os.path.exists(JAVA_OUT):
    shutil.rmtree(JAVA_OUT)

# Recreate the empty deploy folder
os.makedirs(JAVA_OUT)


# Find all Java files
java_files = []

for root, dirs, files in os.walk(JAVA_SRC):
    for file in files:
        if file.endswith(".java"):
            java_files.append(os.path.join(root, file))


# Compile Java
subprocess.run([
    "javac",
    "-d", JAVA_OUT,
    "-cp", PY4J_JAR,
    *java_files
], check=True)


# Start Java Gateway
port = launch_gateway(classpath=JAVA_OUT)

gateway = JavaGateway(
    gateway_parameters=GatewayParameters(port=port)
)

main = gateway.jvm.chess.Main()


# =========================
# Pygame setup
# =========================

pygame.init()

screen = pygame.display.set_mode((800, 800))
pygame.display.set_caption("Pygame + Terminal")

clock = pygame.time.Clock()


# =========================
# Load images
# =========================

black_pawn_img = pygame.transform.scale(
    pygame.image.load("images/BlackPawn.png").convert_alpha(),
    (100, 100)
)

black_bishop_img = pygame.transform.scale(
    pygame.image.load("images/BlackBishop.png").convert_alpha(),
    (100, 100)
)

black_knight_img = pygame.transform.scale(
    pygame.image.load("images/BlackKnight.png").convert_alpha(),
    (100, 100)
)

black_rook_img = pygame.transform.scale(
    pygame.image.load("images/BlackRook.png").convert_alpha(),
    (100, 100)
)

black_queen_img = pygame.transform.scale(
    pygame.image.load("images/BlackQueen.png").convert_alpha(),
    (100, 100)
)

black_king_img = pygame.transform.scale(
    pygame.image.load("images/BlackKing.png").convert_alpha(),
    (100, 100)
)


white_pawn_img = pygame.transform.scale(
    pygame.image.load("images/WhitePawn.png").convert_alpha(),
    (100, 100)
)

white_bishop_img = pygame.transform.scale(
    pygame.image.load("images/WhiteBishop.png").convert_alpha(),
    (100, 100)
)

white_knight_img = pygame.transform.scale(
    pygame.image.load("images/WhiteKnight.png").convert_alpha(),
    (100, 100)
)

white_rook_img = pygame.transform.scale(
    pygame.image.load("images/WhiteRook.png").convert_alpha(),
    (100, 100)
)

white_queen_img = pygame.transform.scale(
    pygame.image.load("images/WhiteQueen.png").convert_alpha(),
    (100, 100)
)

white_king_img = pygame.transform.scale(
    pygame.image.load("images/WhiteKing.png").convert_alpha(),
    (100, 100)
)


# =========================
# Drawing
# =========================

def add_piece(win, piece, x, y):

    piece_type = piece.getName()

    cords = (x * 100, (7 - y) * 100)

    if piece_type == "p":
        win.blit(black_pawn_img, cords)

    elif piece_type == "b":
        win.blit(black_bishop_img, cords)

    elif piece_type == "n":
        win.blit(black_knight_img, cords)

    elif piece_type == "r":
        win.blit(black_rook_img, cords)

    elif piece_type == "q":
        win.blit(black_queen_img, cords)

    elif piece_type == "k":
        win.blit(black_king_img, cords)

    elif piece_type == "P":
        win.blit(white_pawn_img, cords)

    elif piece_type == "B":
        win.blit(white_bishop_img, cords)

    elif piece_type == "N":
        win.blit(white_knight_img, cords)

    elif piece_type == "R":
        win.blit(white_rook_img, cords)

    elif piece_type == "Q":
        win.blit(white_queen_img, cords)

    elif piece_type == "K":
        win.blit(white_king_img, cords)


def draw_board(surface):

    for row in range(8):
        for col in range(8):

            if (row + col) % 2 == 0:
                color = (255, 255, 255)
            else:
                color = (0, 0, 0)

            rect = (
                col * 100,
                row * 100,
                100,
                100
            )

            pygame.draw.rect(surface, color, rect)


# =========================
# Terminal input
# =========================

running = True
# terminal_input = None


# def terminal_thread():

#     global terminal_input
#     global running

#     while running:

#         user_input = input(
#             "Enter your move: "
#         )

#         if (len(user_input) == 4 and user_input.isdigit()) or user_input.lower() == "undo":

#             terminal_input = user_input

#         else:

#             print(
#                 "Invalid input. "
#                 "Please enter a 4-digit number representing the move."
#             )


# # Start terminal thread
# threading.Thread(
#     target=terminal_thread,
#     daemon=True
# ).start()


# =========================
# Main game loop
# =========================

while running:

    # Pygame events
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False


    # # Player turn
    # if not main.isWin():
    #     if main.isWhiteTurn():

    #         if terminal_input is not None:
    #             if terminal_input.lower() == "undo":
    #                 main.undo()
    #                 terminal_input = None
    #                 continue
    #             from_square = int(terminal_input[0:2])
    #             to_square = int(terminal_input[2:4])

    #             if main.isMoveValid(from_square, to_square):

    #                 main.turn(from_square, to_square)

    #             else:

    #                 print("Invalid move!")

    #             # Clear the input regardless of whether
    #             # the move was valid
    #             terminal_input = None


    #     # -------------------------
    #     # Bot turn
    #     # -------------------------

    #     else:

    #         main.botTurn()
    main.update()



    # Get board from Java
    board = main.getBoard()


    # -------------------------
    # Draw board
    # -------------------------

    draw_board(screen)


    for y in range(8):

        for x in range(8):

            if board.isOccupy(x, y):

                piece = board.getSquare(x, y)

                add_piece(
                    screen,
                    piece,
                    x,
                    y
                )


    pygame.display.update()


    # -------------------------
    # FPS
    # -------------------------

    clock.tick(60)


# =========================
# Cleanup
# =========================

pygame.quit()
gateway.close()