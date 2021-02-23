import threading


def run():
    print(1)


thread = threading.Thread(target=run, name="demo")
thread.start()
