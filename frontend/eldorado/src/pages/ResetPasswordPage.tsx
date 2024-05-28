import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Button} from "@/components/ui/button.tsx";
import {z} from "zod";
import {useToast} from "@/components/ui/use-toast.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useNavigate, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Loader2} from "lucide-react";
import {useState} from "react";


function ResetPasswordPage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);

    const formSchema = z.object({
        password: z.string().min(8, {message: t("resetPasswordPage.passwordTooShort")})
            .max(60, {message: t("resetPasswordPage.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            password: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        api.resetPasswordByUser(decodedToken, values.password)
            .then(() => {
                toast({
                    title: t("resetPasswordPage.popUp.resetPasswordOK.title"),
                    description: t("resetPasswordPage.popUp.resetPasswordOK.text"),
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/login', {replace: true});
                            }}>
                                {t("resetPasswordPage.popUp.resetPasswordOK.button")}
                            </Button>
                        </div>
                    ),
                });
            })
            .catch((error) => {
                handleApiError(error);
            }).finally(() => {
            setIsLoading(false);
        });
    }

    return (
        <div>
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <Card className="mx-auto max-w-2xl">
                <CardHeader>
                    <CardTitle>{t("resetPasswordPage.title")}</CardTitle>
                    <CardDescription>{t("resetPasswordPage.info")}</CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)}>
                            <div className="flex flex-col space-y-4">
                                <div>
                                    <FormField
                                        control={form.control}
                                        name="password"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel
                                                    className="text-black">{t("resetPasswordPage.password")}</FormLabel>
                                                <FormControl>
                                                    <Input type="password" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <Button type="submit" className="w-full" disabled={isLoading}>
                                    {isLoading ? (
                                        <>
                                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                        </>
                                    ) : (
                                        t("resetPasswordPage.button")
                                    )}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    );
}

export default ResetPasswordPage;