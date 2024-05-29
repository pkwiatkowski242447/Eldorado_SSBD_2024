import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Loader2} from "lucide-react";


function LoginForm() {
    const {logIn} = useAccount();
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);

    const formSchema = z.object({
        login: z.string().min(5, {message: t("loginPage.loginTooShort")})
            .max(32, {message: t("loginPage.loginTooLong")}),
        password: z.string().min(8, {message: t("loginPage.passwordTooShort")})
            .max(60, {message: t("loginPage.passwordTooLong")})
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            login: "",
            password: "",
        },
    })

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            await logIn(values.login, values.password);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Card className="mx-auto max-w-sm">
            <CardHeader>
                <CardTitle className="text-2xl">{t("loginPage.title")}</CardTitle>
                <CardDescription>
                    {t('loginPage.info')}
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        <FormField
                            control={form.control}
                            name="login"
                            render={({field}) => (
                                <FormItem>
                                    <div className="grid gap-4">
                                        <FormLabel className="text-left">{t("loginPage.login")}</FormLabel>
                                        <FormControl>
                                            <Input placeholder="" {...field} />
                                        </FormControl>
                                        <FormMessage/>
                                    </div>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="password"
                            render={({field}) => (
                                <FormItem>
                                    <div className="grid gap-4">
                                        <div className="flex justify-between items-center">
                                            <FormLabel className="text-left">{t("loginPage.password")}</FormLabel>
                                            <a href="/forgot-password"
                                               className="inline-block text-black underline">
                                                {t('loginPage.forgotPassword')}
                                            </a>
                                        </div>
                                        <FormControl>
                                            <Input type="password" {...field} />
                                        </FormControl>
                                        <FormMessage/>
                                    </div>
                                </FormItem>
                            )}
                        />
                        <Button type="submit" className="w-full" disabled={isLoading}>
                            {isLoading ? (
                                <>
                                    <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                </>
                            ) : (
                                t("loginPage.submit")
                            )}
                        </Button>
                        <div className="mt-4 text-center text-sm">
                            {t("loginPage.noAccount")}
                            <a href="/register" className="font-medium text-black hover:text-blue-500">
                                {t("loginPage.signUp")} </a>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            {t("loginPage.accountSuspended")}
                            <a href="/restore-access" className="font-medium text-black hover:text-blue-500">
                                {t("loginPage.accountSuspended.text")} </a>
                        </div>

                    </form>
                </Form>
            </CardContent>
        </Card>
    )
}

export default LoginForm
