Rewrite of [this repo](https://github.com/il-tmfv/nexign-test-task).
Also it uses a server and compiled styles from the repo mentioned above.

To start the local ClojureScript frontend:
```
lein figwheel
```

Clojure server located [here](https://github.com/il-tmfv/nexign-test-task-server-clojure). Clone it and 
```
STEAM_API_KEY=XXXXX lein ring server-headless 3333
```

-OR-

Clone [this repo](https://github.com/il-tmfv/nexign-test-task) and
```
yarn
STEAM_API_KEY=XXXXX yarn server
```

_XXXXX - your steam API key_
