# copy-deps
A leiningen plugin to fetch dependencies

# Installation

Add copy-deps to plugin list

```clojure
[org.clojars.jj/copy-deps "1.0.0"]
```

## Usage

```bash
lein 
```

### Configuration options

| Option   | Descriptiom                                         | default value  | 
|----------|-----------------------------------------------------|----------------|
| path     | Path where dependencies will be placed              | `./target/lib` |
| strategy | Copy strategy, supported option are :copy and :link | `:copy`        |


## License

Copyright © 2025 [ruroru](https://github.com/ruroru)

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
https://www.eclipse.org/legal/epl-2.0/.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.