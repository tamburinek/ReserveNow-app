import styles from './DocumentationPage.module.scss'
import projectTree from '../../../assets/documentaionPage/projectstructure.png'
import projectTreePages from '../../../assets/documentaionPage/projectstructurePages.png'
import terminologie from '../../../assets/documentaionPage/terminologie.jpg'
import pageStructure from '../../../assets/documentaionPage/pagestucture.png'
import underfolder from '../../../assets/documentaionPage/underfolder.png'
import scrn1 from '../../../assets/documentaionPage/Screenshot_1.png'
import scrn2 from '../../../assets/documentaionPage/Screenshot_2.png'
import scrn3 from '../../../assets/documentaionPage/Screenshot_3.png'

import SyntaxHighlighter from 'react-syntax-highlighter';
import {docco} from 'react-syntax-highlighter/dist/esm/styles/hljs';

const Buttons = () => {
    return (
        <div className={styles.contents}>
            <h1>Components</h1>
            <p>Pokud chcete přidat vlastní styl, stačí udělat .concat:</p>
            <SyntaxHighlighter language="javascript" style={docco}>
                {`<button className={'button-primary sm '.concat(styles.<name>)}>Foo</button>`}
            </SyntaxHighlighter>
            <h2>Buttons</h2>
            <h3>primary</h3>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<button className={'button-primary sm'}>Foo</button>`}
                </SyntaxHighlighter>
                <button className={"button-primary sm"} type={'button'}>Foo</button>
            </div>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<button className={'button-primary lg'}>Foo</button>`}
                </SyntaxHighlighter>
                <button className={"button-primary lg"} type={'button'}>Foo</button>
            </div>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<button className={'button-primary'}>Foo</button>`}
                </SyntaxHighlighter>
                <button className={"button-primary"} type={'button'}>Foo</button>
            </div>
            <h3>outline</h3>
            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<button className={'button-primary-outline'}>Foo</button>`}
                </SyntaxHighlighter>
                <button className={"button-primary-outline"} type={'button'}>Foo</button>
            </div>
            <h3>drop-shadow</h3>
            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<button className={'button-primary bx-sh'}>Foo</button>`}
                </SyntaxHighlighter>
                <button className={'button-primary bx-sh'} type={'button'}>Foo</button>
            </div>
        </div>
    )
}

const Inputs = () => {
    return (
        <div className={styles.contents}>
            <h2>Inputs</h2>
            <h3>primary</h3>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<input className={'input-primary sm'} placeholder={'Foo'}/>`}
                </SyntaxHighlighter>
                <input className={'input-primary sm'} placeholder={'Foo'}/>
            </div>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<input className={'input-primary lg'} placeholder={'Foo'}/>`}
                </SyntaxHighlighter>
                <input className={'input-primary lg'} placeholder={'Foo'}/>
            </div>


            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<input className={'input-primary'} placeholder={'Foo'}/>`}
                </SyntaxHighlighter>
                <input className={'input-primary'} placeholder={'Foo'}/>
            </div>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<input className={'input-primary correct'} placeholder={'Foo'}/>`}
                </SyntaxHighlighter>
                <input className={'input-primary correct'} placeholder={'Foo'}/>
            </div>

            <div className={styles.content}>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`<input className={'input-primary error'} placeholder={'Foo'}/>`}
                </SyntaxHighlighter>
                <input className={'input-primary error'} placeholder={'Foo'}/>
            </div>
        </div>
    )
}

const ProjectStructure = () => {
    return (
        <div className={styles.contents} id={'projectStructue'}>
            <h1>Project structure</h1>
            <div className={styles.ul}>
                <img src={projectTree}/>
                <ul>
                    <li><a href={'#assets'}>assets</a></li>
                    <li><a href={'#config'}>config</a></li>
                    <li><a href={'#pages'}>pages</a></li>
                    <li><a href={'#parts'}>parts</a></li>
                </ul>
                <img src={terminologie} className={styles.terminolog}/>
            </div>
            <br/>
            <h3 id={'assets'}>assets</h3>
            <ul>
                <li>V assets budeme ukládat statické věcy jako jsou obrázky...</li>
            </ul>
            <h3 id={'config'}>config</h3>
            <ul>
                <li>Slouží k ukládání různých configů</li>
                <li>V configu je momentálně jeden soubor, který řeší jestli je aplikace na produkci nebo na localhostu
                </li>
                <li>Nejspíše nebudeme muset žádné konfigurační soubory přidávat</li>
            </ul>
            <h3 id={'pages'}>pages</h3>
            <div className={styles.pages}>
                <img src={projectTreePages}/>
                <ul>
                    <li>Dělíme dále na:
                        <ol>
                            <li>client
                                <ul>
                                    <li>Budeme zde ukládat stránky pro klienta</li>
                                </ul>
                            </li>
                            <li>customer
                                <ul>
                                    <li>Budeme zde ukládat stránky pro zákazníka</li>
                                </ul></li>
                            <li>global
                                <ul>
                                    <li>Budeme zde ukládat stránky jako je index stránka, login, register...</li>
                                </ul>
                            </li>
                        </ol>
                    </li>
                </ul>
            </div>
            <h3 id={'parts'}>parts</h3>
            <ul>
                <li>Zde se budou ukládat komponenty/části aplikace, které se často opakují, například navigation bar
                </li>
            </ul>
        </div>
    )
}

const PageStructure = () => {
    return (
        <div className={styles.contents} id={'pageStructure'}>
            <h1>Page structure & CSS</h1>
            <h3>Page structure</h3>
            <ul>
                <li>Stránky budou ukládány ve složce /pages</li>
                <ul>
                    <li>Pokud se bude jednat o stránku pro klienta, například "vytvoření termínu", tak se stránka uloží
                        v /pages/client
                    </li>
                    <li>Analogicky pokud se bude jednat o stránku pro zákazníka, tak se stránka uloží v
                        /pages/customer
                    </li>
                    <li>Analogicky pro společné stránky, /pages/global</li>
                </ul>
                <br/>
                <li>Každá stránka <strong>má vlastní složku</strong></li>
                <div>
                    <ul>
                        <li>
                            Ve složce bude uloženo jak .html, tak i .css
                        </li>
                        <li>
                            Složka může obsahovat další podsložky
                        </li>
                    </ul>
                    <br/>
                    <div className={styles.flex}>

                        <ul>
                            <li><strong>Příklad:</strong> index-page</li>
                            <ul>
                                <li>index page najdete zde: <a href={'/beta'}>http://localhost:3000/beta</a>
                                </li>
                                <br/>
                                <li>Ve složce tedy najdete stránku jako takovou, IndexPage.js</li>
                                <li>Obsahuje i .css soubor, v našem případě index.module.scss</li>
                                <br/>
                                <li>index-page obsahuje i další složky:
                                    <ul>
                                        <li>home-section</li>
                                        <li>about-section</li>
                                        <li>pricing-section</li>
                                        <li>contact-section</li>
                                        <br/>
                                        <li>Každá podsložka obsahuje vlasní .js a .css module</li>
                                    </ul>
                                </li>
                            </ul>
                        </ul>
                        <img alt={"img"} src={pageStructure}/>
                        <img alt={"img"} src={underfolder}/>
                    </div>
                </div>
            </ul>
            <h3>CSS</h3>
            <ul>
                <li>Classname jednotlivých elementů je dynamicky generovaný, tzn. pokaždé když se načte stránka, tak má
                    jiný className
                </li>
                <li>Každá stránka má vlastní css nebo scss modul</li>
                <br/>
                <li>Dále máme App.scss, což je společný styl pro celou aplikaci, kde budou css rules pro komponenty jako
                    jsou buttony, inputy...
                </li>
            </ul>
        </div>
    )
}

const DemoTutorial = () => {
    return (
        <div className={styles.contents + " " + styles.demo} id={'tutorial'}>
            <h1>Demo Tutorial</h1>
            <ul>
                <li>Uděláme clone projektu, <a href={"https://gitlab.fel.cvut.cz/najmama3/rsp-semestral"}
                                               target={"_blank"}>git</a></li>
                <li>Vytvoříme si novou branch, v gitu pomocí git tool create new branch from origin/main</li>
                <li>Jelikož nejspíše nebudeme mít na lokále složku node_modules, musíme si jí stáhnout <ul>
                    <li>Zadáme v terminálu: <strong>npm install react-scripts@latest</strong></li>
                    <li><strong>!!! Terminál musí být v src/frontend/</strong></li>
                </ul></li>
                <br/>
                <li>Vytvoříme si složku username-test v pages/global</li>
                <li>Vytvoříme si tam dva soubory, Test.js a Test.module.scss, důležitý tam je .module, jinak nebudou
                    fungovat dynamické classy
                </li>
                <img src={scrn1}/>
                <br/>
                <br/>
                <li>Vytvoříme základní strukturu stránky</li>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`const Test = () =>{
        return (
            <div>
                <h1>Ahoj</h1>
                <button>Foo</button>
            </div>
    )
}`}
                </SyntaxHighlighter>
                <br/>
                <li>Zkusíme si zobrazit naší stránku</li>
                <li>Nejdřívě musíme nastavit aby naše const Test stránka se exportovala, takže přidáme před const
                    export
                </li>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`export const Test = () =>{
    ...
}`}
                </SyntaxHighlighter>
                <li>Dále v index.js musíme přidat export naší stránky, přidáme:</li>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`export * from './global/milancu-test/Test'`}
                </SyntaxHighlighter>
                <li>V App.js musíme nastavit na jaký URL se stránka nalinkuje</li>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`
import {..., Test} from "./pages";

...

<BrowserRouter>
    <Routes>
        ...
        <Route exact  path="/milancu-test" element={<Test/>}/>
        ...
    </Routes>
</BrowserRouter>
`}
                </SyntaxHighlighter>
                <br/>
                <li>Teď už si zkusíme zobrazit naší stránku, v terminálu zadáte příkaz: <strong>npm start</strong></li>
                <li><strong>!!! Terminál musí být v src/frontend/</strong></li>
                <li>Až naběhne localhost, zadejte do url: /milancu-test (zadejte vlastní endpoint, který jste si
                    předtím nalinkovali)
                </li>
                <img alt={"img"} src={scrn2} className={styles.border}/>
                <br/>
                <br/>
                <li>Naimportujeme náš vytořený scss jako myStyles (můžeme pojmenovat libovolně)</li>
                <li>Přidáme jednotlivým elementům classNamy a definujme styly</li>
                <div className={styles.flexcodes}>
                    <SyntaxHighlighter language="javascript" style={docco}>
                        {`import myStyles from './Test.module.scss'

export const Test = () =>{
        return (
            <div className={myStyles.contentContainer}>
                <h1 className={myStyles.topic}>Ahoj</h1>
                <button className="button-primary">Foo</button>
            </div>
    )
}`}
                    </SyntaxHighlighter>
                    <SyntaxHighlighter language="javascript" style={docco}>
                        {`.contentContainer {
    display: flex;
    align-items: center;
    justify-content: space-around;

    .topic {
        text-decoration: underline;
    }
}`}
                    </SyntaxHighlighter>
                </div>
                <li>button-primary jsme nemuseli definovat v našem Test.module.scss, protože button už je definovaný ve
                    společným App.scss, více <a href={'#components'}>zde</a></li>
                <li>Takhle by nyní měla vypadat naše stránka</li>
                <img src={scrn3} className={styles.smgimg}/>
                <br/>
                <br/>
                <li>Můžeme buttonu přidat logiku, že když kliknem, tak vyskočí alert okno poté nás to přesměruje na index stránku</li>
                <SyntaxHighlighter language="javascript" style={docco}>
                    {`import myStyles from './Test.module.scss'

export const Test = () => {
    return (
        <div className={myStyles.contentContainer}>
            <h1 className={myStyles.topic}>Ahoj</h1>
            <button
                className="button-primary"
                onClick={() => {
                    alert("Test")
                }}
            >
                <a href="/beta">
                    Foo
                </a>
            </button>
        </div>
    )
}`}
                </SyntaxHighlighter>
            </ul>
        </div>
    )
}

export const DocumentationPage = () => {

    return (
        <div>
            <div className={styles.contents}>
                <h1>Documentation</h1>
                <ul>
                    <li><a href='#projectStructue'>Project Structure</a></li>
                    <li><a href='#pageStructure'>Page structure & CSS</a></li>
                    <li><a href='#components'>Components</a></li>
                    <li><a href='#tutorial'>Demo tutorial</a></li>
                </ul>
            </div>
            <div className={styles.separator}></div>
            <ProjectStructure/>
            <div className={styles.separator}></div>
            <PageStructure/>
            <div className={styles.separator}></div>
            <DemoTutorial/>
            <div className={styles.blackbg} id={'components'}>
                <Buttons/>
                <Inputs/>
            </div>
        </div>
    )
}